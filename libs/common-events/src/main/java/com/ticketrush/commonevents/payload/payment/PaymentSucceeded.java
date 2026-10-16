package com.ticketrush.commonevents.payload.payment;

import java.time.Instant;
import java.util.UUID;

public record PaymentSucceeded(
        UUID paymentId,
        UUID bookingId,
        UUID userId,
        long amountMinor,
        String currency,
        Instant succeededAt
) {
}
