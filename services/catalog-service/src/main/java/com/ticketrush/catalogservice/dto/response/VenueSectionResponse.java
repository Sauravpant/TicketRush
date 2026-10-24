package com.ticketrush.catalogservice.dto.response;

import com.ticketrush.catalogservice.enums.SectionType;

import java.util.UUID;

public record VenueSectionResponse(
    UUID id,
    UUID venueId,
    String name,
    SectionType type,
    Integer capacity
) {}
