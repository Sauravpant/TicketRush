package com.ticketrush.catalogservice.dto.request;

import com.ticketrush.catalogservice.enums.SectionType;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record VenueSectionUpdateRequest(

    @Size(max = 100, message = "Section name must not exceed 100 characters")
    String name,

    SectionType type,

    @Positive(message = "Capacity must be greater than 0")
    Integer capacity
) {}
