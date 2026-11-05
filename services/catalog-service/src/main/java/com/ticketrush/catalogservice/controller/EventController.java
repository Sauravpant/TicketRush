package com.ticketrush.catalogservice.controller;

import com.ticketrush.catalogservice.dto.request.*;
import com.ticketrush.catalogservice.dto.response.PriceTierResponse;
import com.ticketrush.catalogservice.service.PriceTierService;
import com.ticketrush.commonweb.response.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private final PriceTierService priceTierService;

    public EventController(PriceTierService priceTierService) {
        this.priceTierService = priceTierService;
    }

    @PostMapping("/{eventId}/price-tiers")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ApiResponse<PriceTierResponse>> createPriceTier(
            @AuthenticationPrincipal UUID organizerId,
            @PathVariable UUID eventId,
            @Valid @RequestBody PriceTierCreateRequest request ) {

        PriceTierResponse response = priceTierService.createPriceTier(organizerId,eventId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PatchMapping("/{eventId}/price-tiers/{priceTierId}")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ApiResponse<PriceTierResponse>> updatePriceTier(
            @AuthenticationPrincipal UUID organizerId,
            @PathVariable UUID eventId, @PathVariable UUID priceTierId,
            @Valid @RequestBody PriceTierUpdateRequest request) {

        PriceTierResponse response = priceTierService.updatePriceTier(organizerId, eventId, priceTierId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{eventId}/price-tiers/{priceTierId}")
    public ResponseEntity<ApiResponse<PriceTierResponse>> getPriceTier(
            @PathVariable UUID eventId, @PathVariable UUID priceTierId) {

        PriceTierResponse response = priceTierService.getPriceTier(eventId, priceTierId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{eventId}/price-tiers")
    public ResponseEntity<ApiResponse<PageResponse<PriceTierResponse>>> listPriceTiers(
            @PathVariable UUID eventId, @PageableDefault(size = 20) Pageable pageable) {

        PageResponse<PriceTierResponse> response = priceTierService.listPriceTiers( eventId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
