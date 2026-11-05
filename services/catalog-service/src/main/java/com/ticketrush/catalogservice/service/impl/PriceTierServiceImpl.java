package com.ticketrush.catalogservice.service.impl;

import com.ticketrush.catalogservice.dto.request.*;
import com.ticketrush.catalogservice.dto.response.PriceTierResponse;
import com.ticketrush.catalogservice.entity.*;
import com.ticketrush.catalogservice.exception.*;
import com.ticketrush.catalogservice.mapper.PriceTierMapper;
import com.ticketrush.catalogservice.repository.*;  
import com.ticketrush.catalogservice.service.PriceTierService;
import com.ticketrush.commonweb.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PriceTierServiceImpl implements PriceTierService {

    private final PriceTierRepository priceTierRepository;
    private final EventRepository eventRepository;
    private final VenueSectionRepository venueSectionRepository;
    private final PriceTierMapper priceTierMapper;

    public PriceTierServiceImpl(PriceTierRepository priceTierRepository, EventRepository eventRepository, VenueSectionRepository venueSectionRepository,
                                PriceTierMapper priceTierMapper) {
        this.priceTierRepository = priceTierRepository;
        this.eventRepository = eventRepository;
        this.venueSectionRepository = venueSectionRepository;
        this.priceTierMapper = priceTierMapper;
    }

    @Override
    @Transactional
    public PriceTierResponse createPriceTier(UUID organizerId, UUID eventId, PriceTierCreateRequest request) {

        Event event = requireOwnedEvent(organizerId, eventId);

        VenueSection section = venueSectionRepository.findById(request.sectionId())
                .orElseThrow(() -> new VenueSectionNotFoundException(request.sectionId()));

        PriceTier priceTier = PriceTier.builder()
                .event(event)
                .section(section)
                .name(request.name())
                .amountMinor(request.amountMinor())
                .currency(request.currency())
                .build();

        return priceTierMapper.toResponse(priceTierRepository.save(priceTier));
    }

    @Override
    @Transactional
    public PriceTierResponse updatePriceTier(UUID organizerId, UUID eventId, UUID priceTierId, PriceTierUpdateRequest request) {

        requireOwnedEvent(organizerId, eventId);
        PriceTier priceTier = requirePriceTierUnderEvent(eventId, priceTierId);

        if (request.name() != null) priceTier.setName(request.name());
        if (request.amountMinor() != null) priceTier.setAmountMinor(request.amountMinor());
        if (request.currency() != null) priceTier.setCurrency(request.currency());

        return priceTierMapper.toResponse(priceTierRepository.save(priceTier));
    }

    @Override
    public PriceTierResponse getPriceTier(UUID eventId, UUID priceTierId) {

        return priceTierMapper.toResponse(requirePriceTierUnderEvent(eventId, priceTierId));
    }

    @Override
    public PageResponse<PriceTierResponse> listPriceTiers(UUID eventId, Pageable pageable) {
        
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }
        return PageResponse.from(priceTierRepository.findByEventId(eventId, pageable).map(priceTierMapper::toResponse));
    }

    private Event requireOwnedEvent(UUID organizerId, UUID eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (!event.getOrganizerId().equals(organizerId)) {
            throw new OrganizerAccessDeniedException(eventId);
        }

        return event;
    }

    private PriceTier requirePriceTierUnderEvent(UUID eventId, UUID priceTierId) {

        PriceTier priceTier = priceTierRepository.findById(priceTierId)
                .orElseThrow(() -> new PriceTierNotFoundException(priceTierId));

        if (!priceTier.getEvent().getId().equals(eventId)) {
            throw new PriceTierNotFoundException(priceTierId);
        }

        return priceTier;
    }
}