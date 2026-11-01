package com.ticketrush.catalogservice.service.impl;

import com.ticketrush.catalogservice.dto.request.VenueCreateRequest;
import com.ticketrush.catalogservice.dto.request.VenueUpdateRequest;
import com.ticketrush.catalogservice.dto.response.VenueResponse;
import com.ticketrush.catalogservice.entity.Venue;
import com.ticketrush.catalogservice.exception.VenueNotFoundException;
import com.ticketrush.catalogservice.mapper.VenueMapper;
import com.ticketrush.catalogservice.repository.VenueRepository;
import com.ticketrush.catalogservice.service.VenueService;
import com.ticketrush.commonweb.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public VenueServiceImpl(VenueRepository venueRepository, VenueMapper venueMapper) {
        this.venueRepository = venueRepository;
        this.venueMapper = venueMapper;
    }

    @Override
    @Transactional
    public VenueResponse createVenue(VenueCreateRequest request) {
        Venue venue = Venue.builder()
                .name(request.name())
                .city(request.city())
                .address(request.address())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .timezone(request.timezone())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return venueMapper.toResponse(venueRepository.save(venue));
    }

    @Override
    @Transactional
    public VenueResponse updateVenue(UUID venueId, VenueUpdateRequest request) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));

        if (request.name() != null) venue.setName(request.name());
        if (request.city() != null) venue.setCity(request.city());
        if (request.address() != null) venue.setAddress(request.address());
        if (request.latitude() != null) venue.setLatitude(request.latitude());
        if (request.longitude() != null) venue.setLongitude(request.longitude());
        if (request.timezone() != null) venue.setTimezone(request.timezone());
        venue.setUpdatedAt(Instant.now());

        return venueMapper.toResponse(venueRepository.save(venue));
    }

    @Override
    public VenueResponse getVenue(UUID venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));
        return venueMapper.toResponse(venue);
    }

    @Override
    public PageResponse<VenueResponse> listVenues(Pageable pageable) {
        return PageResponse.from(venueRepository.findAll(pageable).map(venueMapper::toResponse));
    }
}