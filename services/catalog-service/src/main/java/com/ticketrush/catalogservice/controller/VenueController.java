package com.ticketrush.catalogservice.controller;

import com.ticketrush.catalogservice.dto.request.*;
import com.ticketrush.catalogservice.dto.response.VenueResponse;
import com.ticketrush.catalogservice.service.VenueService;
import com.ticketrush.commonweb.response.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VenueResponse>> createVenue(@Valid @RequestBody VenueCreateRequest request) {
        VenueResponse response = venueService.createVenue(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PatchMapping("/{venueId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VenueResponse>> updateVenue(@PathVariable UUID venueId, @Valid @RequestBody VenueUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(venueService.updateVenue(venueId, request)));
    }

    @GetMapping("/{venueId}")
    public ResponseEntity<ApiResponse<VenueResponse>> getVenue(@PathVariable UUID venueId) {
        return ResponseEntity.ok(ApiResponse.success(venueService.getVenue(venueId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<VenueResponse>>> listVenues(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(venueService.listVenues(pageable)));
    }
}