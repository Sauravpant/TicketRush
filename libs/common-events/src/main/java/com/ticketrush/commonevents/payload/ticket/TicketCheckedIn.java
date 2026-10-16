package com.ticketrush.commonevents.payload.ticket;

import java.time.Instant;
import java.util.UUID;

public record TicketCheckedIn(
        UUID ticketId,
        UUID bookingId,
        UUID eventId,
        UUID checkedInBy,
        Instant checkedInAt
) {
}

