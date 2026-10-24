package com.ticketrush.catalogservice.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PriceTierUpdateRequest(

    @Size(max = 100, message = "Price tier name must not exceed 100 characters")
    String name,

    @Positive(message = "Amount must be greater than 0")
    Long amountMinor,

    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code (e.g. NPR)")
    String currency
) {}
