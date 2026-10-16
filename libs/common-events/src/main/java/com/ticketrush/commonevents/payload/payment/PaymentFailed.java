package com.ticketrush.commonevents.payload.payment;

import java.time.Instant;
import java.util.UUID;

public record PaymentFailed(
        UUID paymentId,
        UUID bookingId,
        String failureCode,
        String reason,
        Instant failedAt
) {
}
