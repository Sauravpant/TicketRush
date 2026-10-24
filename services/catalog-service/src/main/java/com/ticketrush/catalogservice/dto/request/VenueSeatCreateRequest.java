package com.ticketrush.catalogservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record VenueSeatCreateRequest(

    @NotNull(message = "Section ID is required")
    UUID sectionId,

    @Size(max = 10, message = "Row label must not exceed 10 characters")
    String rowLabel,

    @NotNull(message = "Seat number is required")
    @Positive(message = "Seat number must be greater than 0")
    Integer seatNumber
) {}
