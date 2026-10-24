package com.ticketrush.catalogservice.exception;

import java.util.UUID;

public class OrganizerAccessDeniedException extends RuntimeException {
    public OrganizerAccessDeniedException(UUID eventId) {
        super("You do not have permission to modify event: " + eventId);
    }
}
