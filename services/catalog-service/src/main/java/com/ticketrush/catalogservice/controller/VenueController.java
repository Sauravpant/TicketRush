package com.ticketrush.catalogservice.controller;

import com.ticketrush.catalogservice.dto.request.*;
import com.ticketrush.catalogservice.dto.response.VenueResponse;
import com.ticketrush.catalogservice.dto.response.VenueSeatResponse;
import com.ticketrush.catalogservice.dto.response.VenueSectionResponse;
import com.ticketrush.catalogservice.service.VenueSeatService;
import com.ticketrush.catalogservice.service.VenueSectionService;
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
    private final VenueSeatService venueSeatService;
    private final VenueSectionService venueSectionService;

    public VenueController(VenueService venueService, VenueSectionService venueSectionService, VenueSeatService venueSeatService) {
        this.venueService = venueService;
        this.venueSeatService = venueSeatService;
        this.venueSectionService = venueSectionService;
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

    // ── Sections ──
    @PostMapping("/{venueId}/sections")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VenueSectionResponse>> createSection(
            @PathVariable UUID venueId, @Valid @RequestBody VenueSectionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(venueSectionService.createSection(venueId, request)));
    }

    @PatchMapping("/{venueId}/sections/{sectionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VenueSectionResponse>> updateSection(
            @PathVariable UUID venueId, @PathVariable UUID sectionId,
            @Valid @RequestBody VenueSectionUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(venueSectionService.updateSection(venueId, sectionId, request)));
    }

    @GetMapping("/{venueId}/sections/{sectionId}")
    public ResponseEntity<ApiResponse<VenueSectionResponse>> getSection(
            @PathVariable UUID venueId, @PathVariable UUID sectionId) {
        return ResponseEntity.ok(ApiResponse.success(venueSectionService.getSection(venueId, sectionId)));
    }

    @GetMapping("/{venueId}/sections")
    public ResponseEntity<ApiResponse<PageResponse<VenueSectionResponse>>> listSections(
            @PathVariable UUID venueId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(venueSectionService.listSections(venueId, pageable)));
    }

    // ── Seats ──
    @PostMapping("/{venueId}/sections/{sectionId}/seats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VenueSeatResponse>> createSeat(
            @PathVariable UUID venueId, @PathVariable UUID sectionId,
            @Valid @RequestBody VenueSeatCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(venueSeatService.createSeat(venueId, sectionId, request)));
    }

    @GetMapping("/{venueId}/sections/{sectionId}/seats/{seatId}")
    public ResponseEntity<ApiResponse<VenueSeatResponse>> getSeat(
            @PathVariable UUID venueId, @PathVariable UUID sectionId, @PathVariable UUID seatId) {
        return ResponseEntity.ok(ApiResponse.success(venueSeatService.getSeat(venueId, sectionId, seatId)));
    }

    @GetMapping("/{venueId}/sections/{sectionId}/seats")
    public ResponseEntity<ApiResponse<PageResponse<VenueSeatResponse>>> listSeats(
            @PathVariable UUID venueId, @PathVariable UUID sectionId,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(venueSeatService.listSeats(venueId, sectionId, pageable)));
    }


}