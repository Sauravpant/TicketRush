package com.ticketrush.commonevents.payload.inventory;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SeatsReleased(
        UUID bookingId,
        UUID eventId,
        List<UUID> seatIds,
        String reason,
        Instant releasedAt
) {
}
