package com.ticketrush.catalogservice.dto.request;

import com.ticketrush.catalogservice.enums.SectionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record VenueSectionCreateRequest(

    @NotNull(message = "Venue ID is required")
    UUID venueId,

    @NotBlank(message = "Section name is required")
    @Size(max = 100, message = "Section name must not exceed 100 characters")
    String name,

    @NotNull(message = "Section type is required (SEATED or GENERAL_ADMISSION)")
    SectionType type,

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than 0")
    Integer capacity
) {}
