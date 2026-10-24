package com.ticketrush.catalogservice.dto.response;

import java.time.Instant;
import java.util.UUID;

public record VenueResponse(
    UUID id,
    String name,
    String city,
    String address,
    Double latitude,
    Double longitude,
    String timezone,
    Instant createdAt,
    Instant updatedAt
) {}
