package com.ticketrush.catalogservice.exception;

import java.util.UUID;

public class VenueSectionNotFoundException extends RuntimeException {
    public VenueSectionNotFoundException(UUID id) {
        super("Venue section not found: " + id);
    }
}
