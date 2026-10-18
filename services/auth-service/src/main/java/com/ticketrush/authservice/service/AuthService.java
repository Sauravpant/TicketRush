package com.ticketrush.authservice.service;

import com.ticketrush.authservice.dto.request.LoginRequest;
import com.ticketrush.authservice.dto.request.RegisterRequest;
import com.ticketrush.authservice.dto.response.UserResponse;
import java.util.UUID;

public interface AuthService {
    UserResponse register(RegisterRequest request);

    TokenPair login(LoginRequest request, String userAgent, String ip);

    TokenPair refresh(String rawRefreshToken, String userAgent, String ip);

    void logout(String rawRefreshToken);

    void logoutAllDevices(UUID userId);

    record TokenPair(String accessToken, String rawRefreshToken, long accessTokenTtlSeconds, UserResponse user) {
    }
}