package com.ticketrush.commonevents.payload.booking;

import java.time.Instant;
import java.util.UUID;

public record BookingConfirmed(
        UUID bookingId,
        UUID userId,
        UUID eventId,
        UUID paymentId,
        long amountMinor,
        String currency,
        String contactEmail,
        Instant confirmedAt
) {
}
