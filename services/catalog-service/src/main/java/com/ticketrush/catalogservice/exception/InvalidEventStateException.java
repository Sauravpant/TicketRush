package com.ticketrush.catalogservice.exception;

import com.ticketrush.catalogservice.enums.EventStatus;
import java.util.UUID;

public class InvalidEventStateException extends RuntimeException {
    public InvalidEventStateException(UUID eventId, EventStatus currentStatus) {
        super("Event " + eventId + " cannot transition from state " + currentStatus);
    }
}