package com.ticketrush.catalogservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ticketrush.catalogservice.dto.request.*;
import com.ticketrush.catalogservice.dto.response.*;
import com.ticketrush.catalogservice.entity.*;
import com.ticketrush.catalogservice.enums.*;
import com.ticketrush.catalogservice.exception.*;
import com.ticketrush.catalogservice.mapper.EventMapper;
import com.ticketrush.catalogservice.messaging.CatalogEventPublisher;
import com.ticketrush.catalogservice.repository.*;
import com.ticketrush.catalogservice.validator.EventPublishingValidator;
import com.ticketrush.catalogservice.service.EventService;
import com.ticketrush.commonweb.response.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private static final String CACHE_KEY_PREFIX = "catalog:event:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final PriceTierRepository priceTierRepository;
    private final EventMapper eventMapper;
    private final CatalogEventPublisher catalogEventPublisher;
    private final EventPublishingValidator eventPublishingValidator;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Cloudinary cloudinary;

    public EventServiceImpl(EventRepository eventRepository,
                            VenueRepository venueRepository,
                            PriceTierRepository priceTierRepository,
                            EventMapper eventMapper,
                            CatalogEventPublisher catalogEventPublisher,
                            EventPublishingValidator eventPublishingValidator,
                            RedisTemplate<String, Object> redisTemplate,
                            Cloudinary cloudinary) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.priceTierRepository = priceTierRepository;
        this.eventMapper = eventMapper;
        this.catalogEventPublisher = catalogEventPublisher;
        this.eventPublishingValidator = eventPublishingValidator;
        this.redisTemplate = redisTemplate;
        this.cloudinary = cloudinary;
    }

    @Override
    @Transactional
    public EventResponse createEvent(UUID organizerId, EventCreateRequest request, MultipartFile coverImage) {

        String coverImagePublicId = null;
        String coverImageAssetId = null;

        if (coverImage != null && !coverImage.isEmpty()) {
            Map<?, ?> uploadResult = uploadCoverImage(coverImage);
            coverImagePublicId = (String) uploadResult.get("public_id");
            coverImageAssetId = (String) uploadResult.get("asset_id");
        }

        Venue venue = venueRepository.findById(request.venueId())
                .orElseThrow(() -> new VenueNotFoundException(request.venueId()));

        Event event = Event.builder()
                .organizerId(organizerId)
                .venue(venue)
                .title(request.title())
                .shortDescription(request.shortDescription())
                .description(request.description())
                .category(request.category())
                .coverImagePublicId(coverImagePublicId)
                .coverImageAssetId(coverImageAssetId)
                .startsAt(request.startsAt())
                .endsAt(request.endsAt())
                .salesOpenAt(request.salesOpenAt())
                .salesCloseAt(request.salesCloseAt())
                .status(EventStatus.DRAFT)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventResponse updateEvent(UUID organizerId, UUID eventId, EventUpdateRequest request) {
        Event event = requireOwnedEvent(organizerId, eventId);

        if (request.title() != null) event.setTitle(request.title());
        if (request.shortDescription() != null) event.setShortDescription(request.shortDescription());
        if (request.description() != null) event.setDescription(request.description());
        if (request.category() != null) event.setCategory(request.category());
        if (request.startsAt() != null) event.setStartsAt(request.startsAt());
        if (request.endsAt() != null) event.setEndsAt(request.endsAt());
        if (request.salesOpenAt() != null) event.setSalesOpenAt(request.salesOpenAt());
        if (request.salesCloseAt() != null) event.setSalesCloseAt(request.salesCloseAt());
        event.setUpdatedAt(Instant.now());

        event = eventRepository.save(event);
        evictCache(eventId);

        if (event.getStatus() == EventStatus.PUBLISHED) {
            List<PriceTier> priceTiers = priceTierRepository.findByEventId(eventId);
            catalogEventPublisher.publishEventUpdated(event, event.getVenue(), priceTiers);
        }

        return eventMapper.toResponse(event);
    }

    @Override
    @Transactional
    public EventResponse publishEvent(UUID organizerId, UUID eventId) {
        Event event = requireOwnedEvent(organizerId, eventId);
        List<PriceTier> priceTiers = priceTierRepository.findByEventId(eventId);

        eventPublishingValidator.validate(event, priceTiers);

        event.setStatus(EventStatus.PUBLISHED);
        event.setUpdatedAt(Instant.now());
        event = eventRepository.save(event);
        evictCache(eventId);

        catalogEventPublisher.publishEventPublished(event, event.getVenue(), priceTiers);

        return eventMapper.toResponse(event);
    }

    @Override
    @Transactional
    public EventResponse cancelEvent(UUID organizerId, UUID eventId, String reason) {
        Event event = requireOwnedEvent(organizerId, eventId);

        if (event.getStatus() == EventStatus.CANCELLED) {
            throw new InvalidEventStateException(eventId, event.getStatus());
        }

        boolean wasPublished = event.getStatus() == EventStatus.PUBLISHED;

        event.setStatus(EventStatus.CANCELLED);
        event.setUpdatedAt(Instant.now());
        event = eventRepository.save(event);
        evictCache(eventId);

        if (wasPublished) {
            catalogEventPublisher.publishEventCancelled(event, reason);
        }

        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse getEvent(UUID eventId) {
        String cacheKey = CACHE_KEY_PREFIX + eventId;

        EventResponse cached = readFromCache(cacheKey);
        if (cached != null) {
            return cached;
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        EventResponse response = eventMapper.toResponse(event);

        writeToCache(cacheKey, response);
        return response;
    }

    @Override
    public PageResponse<EventResponse> listEvents(Pageable pageable) {
        // Deliberately NOT cached — only single-event GETs are cache-aside per the architecture plan.
        return PageResponse.from(eventRepository.findAll(pageable).map(eventMapper::toResponse));
    }

    private Event requireOwnedEvent(UUID organizerId, UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (!event.getOrganizerId().equals(organizerId)) {
            throw new OrganizerAccessDeniedException(eventId);
        }

        return event;
    }

    private Map<?, ?> uploadCoverImage(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "ticketrush/events"));
        } catch (IOException ex) {
            throw new ImageUploadException("Failed to upload cover image", ex);
        }
    }

    private EventResponse readFromCache(String cacheKey) {
        try {
            return (EventResponse) redisTemplate.opsForValue().get(cacheKey);
        } catch (Exception ex) {
            log.warn("Redis read failed for key {}, falling back to DB", cacheKey, ex);
            return null;
        }
    }

    private void writeToCache(String cacheKey, EventResponse response) {
        try {
            redisTemplate.opsForValue().set(cacheKey, response, CACHE_TTL);
        } catch (Exception ex) {
            log.warn("Redis write failed for key {}", cacheKey, ex);
        }
    }

    private void evictCache(UUID eventId) {
        try {
            redisTemplate.delete(CACHE_KEY_PREFIX + eventId);
        } catch (Exception ex) {
            log.warn("Redis evict failed for event {}", eventId, ex);
        }
    }
}