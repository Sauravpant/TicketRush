package com.ticketrush.catalogservice.exception;

import java.util.UUID;

public class EventNotPublishableException extends RuntimeException {
    public EventNotPublishableException(UUID eventId, String reason) {
        super("Event " + eventId + " cannot be published: " + reason);
    }
}