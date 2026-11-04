package com.ticketrush.catalogservice.service.impl;

import com.ticketrush.catalogservice.dto.request.VenueSeatCreateRequest;
import com.ticketrush.catalogservice.dto.response.VenueSeatResponse;
import com.ticketrush.catalogservice.entity.*;
import com.ticketrush.catalogservice.exception.*;
import com.ticketrush.catalogservice.mapper.VenueSeatMapper;
import com.ticketrush.catalogservice.repository.*;
import com.ticketrush.catalogservice.service.VenueSeatService;
import com.ticketrush.commonweb.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class VenueSeatServiceImpl implements VenueSeatService {

    private final VenueSeatRepository venueSeatRepository;
    private final VenueSectionRepository venueSectionRepository;
    private final VenueRepository venueRepository;
    private final VenueSeatMapper venueSeatMapper;

    public VenueSeatServiceImpl(VenueSeatRepository venueSeatRepository, VenueSectionRepository venueSectionRepository, VenueRepository venueRepository,
                                VenueSeatMapper venueSeatMapper) {
        this.venueSeatRepository = venueSeatRepository;
        this.venueSectionRepository = venueSectionRepository;
        this.venueRepository = venueRepository;
        this.venueSeatMapper = venueSeatMapper;
    }

    @Override
    @Transactional
    public VenueSeatResponse createSeat(UUID venueId, UUID sectionId, VenueSeatCreateRequest request) {

        VenueSection section = requireSectionUnderVenue(venueId, sectionId);

        VenueSeat seat = VenueSeat.builder()
                .section(section)
                .rowLabel(request.rowLabel())
                .seatNumber(request.seatNumber())
                .build();

        return venueSeatMapper.toResponse(venueSeatRepository.save(seat));
    }

    @Override
    public VenueSeatResponse getSeat(UUID venueId, UUID sectionId, UUID seatId) {

        requireSectionUnderVenue(venueId, sectionId);

        VenueSeat seat = venueSeatRepository.findById(seatId)
                .orElseThrow(() -> new VenueSeatNotFoundException(seatId));

        if (!seat.getSection().getId().equals(sectionId)) {
            throw new VenueSeatNotFoundException(seatId);
        }

        return venueSeatMapper.toResponse(seat);
    }

    @Override
    public PageResponse<VenueSeatResponse> listSeats(UUID venueId, UUID sectionId, Pageable pageable) {

        requireSectionUnderVenue(venueId, sectionId);

        return PageResponse.from(
                venueSeatRepository.findBySectionId(sectionId, pageable).map(venueSeatMapper::toResponse));
    }

    private VenueSection requireSectionUnderVenue(UUID venueId, UUID sectionId) {
        
        venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException(venueId));

        VenueSection section = venueSectionRepository.findById(sectionId)
                .orElseThrow(() -> new VenueSectionNotFoundException(sectionId));

        if (!section.getVenue().getId().equals(venueId)) {
            throw new VenueSectionNotFoundException(sectionId);
        }

        return section;
    }
}