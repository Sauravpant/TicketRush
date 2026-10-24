package com.ticketrush.catalogservice.exception;

import java.util.UUID;

public class PriceTierNotFoundException extends RuntimeException {
    public PriceTierNotFoundException(UUID id) {
        super("Price tier not found: " + id);
    }
}
