package com.ticketrush.commonevents.payload.booking;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record BookingCreated(
        UUID bookingId,
        UUID userId,
        UUID eventId,
        List<UUID> seatIds,
        long amountMinor,
        String currency,
        String contactEmail,
        Instant createdAt
) {
}
