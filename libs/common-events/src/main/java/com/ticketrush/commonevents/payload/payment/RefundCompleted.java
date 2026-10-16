package com.ticketrush.commonevents.payload.payment;

import java.time.Instant;
import java.util.UUID;

public record RefundCompleted(
        UUID refundId,
        UUID paymentId,
        UUID bookingId,
        long amountMinor,
        String currency,
        Instant completedAt
) {
}
