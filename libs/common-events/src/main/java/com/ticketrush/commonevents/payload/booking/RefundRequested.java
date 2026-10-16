package com.ticketrush.commonevents.payload.booking;

import java.time.Instant;
import java.util.UUID;

public record RefundRequested(
        UUID refundId,
        UUID bookingId,
        UUID paymentId,
        long amountMinor,
        String currency,
        String reason,
        Instant requestedAt
) {
}
