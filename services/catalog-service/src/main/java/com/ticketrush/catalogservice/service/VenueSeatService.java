package com.ticketrush.catalogservice.service;

import com.ticketrush.catalogservice.dto.request.VenueSeatCreateRequest;
import com.ticketrush.catalogservice.dto.response.VenueSeatResponse;
import com.ticketrush.commonweb.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface VenueSeatService {

    VenueSeatResponse createSeat(UUID venueId, UUID sectionId, VenueSeatCreateRequest request);
    VenueSeatResponse getSeat(UUID venueId, UUID sectionId, UUID seatId);
    PageResponse<VenueSeatResponse> listSeats(UUID venueId, UUID sectionId, Pageable pageable);
}
