package com.ticketrush.commonevents.payload.inventory;

import java.time.Instant;
import java.util.UUID;

public record EventSoldOut(
        UUID eventId,
        Instant soldOutAt
) {
}
