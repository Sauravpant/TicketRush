package com.ticketrush.catalogservice.dto.response;

import com.ticketrush.catalogservice.enums.EventStatus;

import java.time.Instant;
import java.util.UUID;

public record EventResponse(
    UUID id,
    UUID organizerId,
    UUID venueId,
    String title,
    String shortDescription,
    String description,
    String category,
    String coverImagePublicId,
    Instant startsAt,
    Instant endsAt,
    Instant salesOpenAt,
    Instant salesCloseAt,
    EventStatus status,
    Long version,
    Instant createdAt,
    Instant updatedAt
) {}
