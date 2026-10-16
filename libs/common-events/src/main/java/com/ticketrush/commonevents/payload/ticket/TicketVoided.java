package com.ticketrush.commonevents.payload.ticket;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TicketVoided(
        UUID bookingId,
        UUID eventId,
        List<UUID> ticketIds,
        String reason,
        Instant voidedAt
) {
}
