package com.ticketrush.commonevents.payload.booking;

import java.time.Instant;
import java.util.UUID;

public record BookingFailed(
        UUID bookingId,
        UUID userId,
        UUID eventId,
        String failureCode,
        String reason,
        Instant failedAt
) {
}
