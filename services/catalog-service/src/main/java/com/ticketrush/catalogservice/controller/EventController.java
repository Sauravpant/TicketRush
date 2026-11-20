package com.ticketrush.catalogservice.controller;

import com.ticketrush.catalogservice.dto.request.*;
import com.ticketrush.catalogservice.dto.response.EventResponse;
import com.ticketrush.catalogservice.dto.response.PriceTierResponse;
import com.ticketrush.catalogservice.service.*;
import com.ticketrush.commonweb.response.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private final PriceTierService priceTierService;
    private final EventService eventService;

    public EventController(PriceTierService priceTierService, EventService eventService) {
        this.priceTierService = priceTierService;
        this.eventService = eventService;
    }

    @PostMapping("/{eventId}/price-tiers")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ApiResponse<PriceTierResponse>> createPriceTier(@AuthenticationPrincipal UUID organizerId, @PathVariable UUID eventId, @Valid @RequestBody PriceTierCreateRequest request) {

        PriceTierResponse response = priceTierService.createPriceTier(organizerId, eventId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PatchMapping("/{eventId}/price-tiers/{priceTierId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ApiResponse<PriceTierResponse>> updatePriceTier(@AuthenticationPrincipal UUID organizerId, @PathVariable UUID eventId, @PathVariable UUID priceTierId, @Valid @RequestBody PriceTierUpdateRequest request) {

        PriceTierResponse response = priceTierService.updatePriceTier(organizerId, eventId, priceTierId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{eventId}/price-tiers/{priceTierId}")
    public ResponseEntity<ApiResponse<PriceTierResponse>> getPriceTier(@PathVariable UUID eventId, @PathVariable UUID priceTierId) {
        
        PriceTierResponse response = priceTierService.getPriceTier(eventId, priceTierId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{eventId}/price-tiers")
    public ResponseEntity<ApiResponse<PageResponse<PriceTierResponse>>> listPriceTiers(@PathVariable UUID eventId, @PageableDefault(size = 20) Pageable pageable) {

        PageResponse<PriceTierResponse> response = priceTierService.listPriceTiers(eventId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(@AuthenticationPrincipal UUID organizerId, @RequestPart("request") @Valid EventCreateRequest request, @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {

        EventResponse response = eventService.createEvent(organizerId, request, coverImage);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PatchMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(@AuthenticationPrincipal UUID organizerId, @PathVariable UUID eventId, @Valid @RequestBody EventUpdateRequest request) {

        EventResponse response = eventService.updateEvent(organizerId, eventId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{eventId}/publish")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ApiResponse<EventResponse>> publishEvent(@AuthenticationPrincipal UUID organizerId, @PathVariable UUID eventId) {

        EventResponse response = eventService.publishEvent(organizerId, eventId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{eventId}/cancel")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ApiResponse<EventResponse>> cancelEvent(@AuthenticationPrincipal UUID organizerId, @PathVariable UUID eventId, @RequestParam(required = false) String reason) {

        EventResponse response = eventService.cancelEvent(organizerId, eventId, reason);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> getEvent(@PathVariable UUID eventId) {

        EventResponse response = eventService.getEvent(eventId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<EventResponse>>> listEvents(@PageableDefault(size = 20) Pageable pageable) {
        
        PageResponse<EventResponse> response = eventService.listEvents(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}