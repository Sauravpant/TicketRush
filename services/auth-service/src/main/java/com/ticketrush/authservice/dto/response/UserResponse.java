package com.ticketrush.authservice.dto.response;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String fullName,
        String phoneNumber,
        Instant createdAt,
        Instant updatedAt,
        Set<String> roles
) {

}