package com.ticketrush.commonevents.payload.inventory;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SeatHoldExpired(
        UUID bookingId,
        UUID eventId,
        List<UUID> seatIds,
        Instant expiredAt
) {
}
