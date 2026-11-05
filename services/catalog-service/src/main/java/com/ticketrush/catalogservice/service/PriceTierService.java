package com.ticketrush.catalogservice.service;

import com.ticketrush.catalogservice.dto.request.PriceTierCreateRequest;
import com.ticketrush.catalogservice.dto.request.PriceTierUpdateRequest;
import com.ticketrush.catalogservice.dto.response.PriceTierResponse;
import com.ticketrush.commonweb.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PriceTierService {

    PriceTierResponse createPriceTier(UUID organizerId, UUID eventId, PriceTierCreateRequest request);
    PriceTierResponse updatePriceTier(UUID organizerId, UUID eventId, UUID priceTierId, PriceTierUpdateRequest request);
    PriceTierResponse getPriceTier(UUID eventId, UUID priceTierId);
    PageResponse<PriceTierResponse> listPriceTiers(UUID eventId, Pageable pageable);
}

