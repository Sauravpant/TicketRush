package com.ticketrush.catalogservice.service.impl;

import com.ticketrush.catalogservice.dto.request.*;
import com.ticketrush.catalogservice.dto.response.*;
import com.ticketrush.catalogservice.entity.*;
import com.ticketrush.catalogservice.exception.*;
import com.ticketrush.catalogservice.mapper.VenueSectionMapper;
import com.ticketrush.catalogservice.repository.*;
import com.ticketrush.catalogservice.service.VenueSectionService;
import com.ticketrush.commonweb.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class VenueSectionServiceImpl implements VenueSectionService {

    private final VenueSectionRepository venueSectionRepository;
    private final VenueRepository venueRepository;
    private final VenueSectionMapper venueSectionMapper;

    public VenueSectionServiceImpl(VenueSectionRepository venueSectionRepository, VenueRepository venueRepository, VenueSectionMapper venueSectionMapper) {
        this.venueSectionRepository = venueSectionRepository;
        this.venueRepository = venueRepository;
        this.venueSectionMapper = venueSectionMapper;
    }

    @Override
    @Transactional
    public VenueSectionResponse createSection(UUID venueId, VenueSectionCreateRequest request) {
        
        Venue venue = requireVenue(venueId);

        VenueSection section = VenueSection.builder()
                .venue(venue)
                .name(request.name())
                .type(request.type())
                .capacity(request.capacity())
                .build();

        return venueSectionMapper.toResponse(venueSectionRepository.save(section));
    }

    @Override
    @Transactional
    public VenueSectionResponse updateSection(UUID venueId, UUID sectionId, VenueSectionUpdateRequest request) {
        requireVenue(venueId);
        VenueSection section = requireSectionUnderVenue(venueId, sectionId);

        if (request.name() != null) section.setName(request.name());
        if (request.type() != null) section.setType(request.type());
        if (request.capacity() != null) section.setCapacity(request.capacity());

        return venueSectionMapper.toResponse(venueSectionRepository.save(section));
    }

    @Override
    public VenueSectionResponse getSection(UUID venueId, UUID sectionId) {
        requireVenue(venueId);
        return venueSectionMapper.toResponse(requireSectionUnderVenue(venueId, sectionId));
    }

    @Override
    public PageResponse<VenueSectionResponse> listSections(UUID venueId, Pageable pageable) {
        requireVenue(venueId);
        return PageResponse.from(
                venueSectionRepository.findByVenueId(venueId, pageable).map(venueSectionMapper::toResponse));
    }

    private Venue requireVenue(UUID venueId) {
        return venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));
    }

    private VenueSection requireSectionUnderVenue(UUID venueId, UUID sectionId) {
        VenueSection section = venueSectionRepository.findById(sectionId)
                .orElseThrow(() -> new VenueSectionNotFoundException(sectionId));

        if (!section.getVenue().getId().equals(venueId)) {
            throw new VenueSectionNotFoundException(sectionId);
        }

        return section;
    }
}