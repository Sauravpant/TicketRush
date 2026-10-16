package com.ticketrush.commonevents.payload.booking;

import java.time.Instant;
import java.util.UUID;

public record BookingExpired(
        UUID bookingId,
        UUID userId,
        UUID eventId,
        Instant expiredAt
) {
}
