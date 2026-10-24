package com.ticketrush.catalogservice.exception;

import java.util.UUID;

public class VenueSeatNotFoundException extends RuntimeException {
    public VenueSeatNotFoundException(UUID id) {
        super("Venue seat not found: " + id);
    }
}
