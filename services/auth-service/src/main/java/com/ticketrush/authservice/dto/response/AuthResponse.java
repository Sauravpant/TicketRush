package com.ticketrush.authservice.dto.response;

public record AuthResponse(
        String accessToken,
        long expiresInSeconds,
        UserResponse user
) {
}