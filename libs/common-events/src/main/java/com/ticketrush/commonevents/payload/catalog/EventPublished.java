package com.ticketrush.commonevents.payload.catalog;

import java.time.Instant;
import java.util.UUID;

public record EventPublished(
        UUID eventId,
        UUID organizerId,
        String title,
        String description,
        String category,
        UUID venueId,
        String venueName,
        String city,
        Instant startsAt,
        Instant endsAt,
        long minPriceMinor,
        long maxPriceMinor,
        long version
) {
}
