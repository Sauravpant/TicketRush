package com.ticketrush.catalogservice.service;

import com.ticketrush.catalogservice.dto.request.VenueCreateRequest;
import com.ticketrush.catalogservice.dto.request.VenueUpdateRequest;
import com.ticketrush.catalogservice.dto.response.VenueResponse;
import com.ticketrush.commonweb.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface VenueService {

    VenueResponse createVenue(VenueCreateRequest request);
    VenueResponse updateVenue(UUID venueId, VenueUpdateRequest request);
    VenueResponse getVenue(UUID venueId);
    PageResponse<VenueResponse> listVenues(Pageable pageable);
}