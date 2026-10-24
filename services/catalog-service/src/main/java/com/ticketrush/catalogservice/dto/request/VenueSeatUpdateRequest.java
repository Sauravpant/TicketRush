package com.ticketrush.catalogservice.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record VenueSeatUpdateRequest(

    @Size(max = 10, message = "Row label must not exceed 10 characters")
    String rowLabel,

    @Positive(message = "Seat number must be greater than 0")
    Integer seatNumber
) {}
