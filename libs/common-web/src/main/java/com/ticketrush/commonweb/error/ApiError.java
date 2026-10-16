package com.ticketrush.commonweb.error;

import java.time.Instant;
import java.util.List;

public record ApiError(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        String correlationId,
        Instant timestamp,
        List<FieldErrorDetail> errors) {

    public ApiError {
        timestamp = timestamp == null ? Instant.now() : timestamp;
        errors = errors == null ? List.of() : List.copyOf(errors);
    }

    public record FieldErrorDetail(
            String field,
            String message) {
    }

}
