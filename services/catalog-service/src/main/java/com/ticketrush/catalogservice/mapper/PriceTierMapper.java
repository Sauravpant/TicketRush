package com.ticketrush.catalogservice.mapper;

import com.ticketrush.catalogservice.dto.response.PriceTierResponse;
import com.ticketrush.catalogservice.entity.PriceTier;
import org.springframework.stereotype.Component;

@Component
public class PriceTierMapper {

    public PriceTierResponse toResponse(PriceTier priceTier) {
        return new PriceTierResponse(
                priceTier.getId(),
                priceTier.getEvent().getId(),
                priceTier.getSection().getId(),
                priceTier.getName(),
                priceTier.getAmountMinor(),
                priceTier.getCurrency()
        );
    }
}