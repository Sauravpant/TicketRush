package com.ticketrush.commonevents.payload.ticket;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TicketsIssued(
        UUID bookingId,
        UUID userId,
        UUID eventId,
        List<UUID> ticketIds,
        String contactEmail,
        Instant issuedAt
) {
}
