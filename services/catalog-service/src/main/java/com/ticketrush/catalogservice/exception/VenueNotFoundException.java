package com.ticketrush.catalogservice.exception;

import java.util.UUID;

public class VenueNotFoundException extends RuntimeException {
    public VenueNotFoundException(UUID id) {
        super("Venue not found: " + id);
    }
}
