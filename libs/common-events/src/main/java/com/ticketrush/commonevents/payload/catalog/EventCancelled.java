package com.ticketrush.commonevents.payload.catalog;

import java.time.Instant;
import java.util.UUID;

public record EventCancelled(
        UUID eventId,
        String reason,
        Instant cancelledAt,
        long version
) {
}
