package com.ticketrush.authservice.controller;

import com.ticketrush.authservice.dto.request.LoginRequest;
import com.ticketrush.authservice.dto.request.RegisterRequest;
import com.ticketrush.authservice.dto.response.AuthResponse;
import com.ticketrush.authservice.dto.response.UserResponse;
import com.ticketrush.authservice.exception.InvalidRefreshTokenException;
import com.ticketrush.authservice.security.CookieGenerator;
import com.ticketrush.authservice.service.AuthService;
import com.ticketrush.commonweb.response.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieGenerator cookieGenerator;

    public AuthController(AuthService authService, CookieGenerator cookieGenerator) {
        this.authService = authService;
        this.cookieGenerator = cookieGenerator;
    }

    @Value("${jwt.refresh-token-ttl-days}")
    private long refreshTokenTtlDays;

    @Value("${auth.cookie.refresh-token-name}")
    private String refreshTokenCookieName;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = authService.register(request);
        return ResponseEntity.status(201).body(ApiResponse.success("User registered successfully", user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        var pair = authService.login(request, httpRequest.getHeader("User-Agent"), httpRequest.getRemoteAddr());
        setAuthCookies(httpResponse, pair.accessToken(), pair.rawRefreshToken(), pair.accessTokenTtlSeconds());
        AuthResponse body = new AuthResponse(pair.accessToken(), pair.accessTokenTtlSeconds(), pair.user());

        return ResponseEntity.ok(ApiResponse.success("Login successful", body));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        String rawRefreshToken = extractRefreshToken(httpRequest);
        var pair = authService.refresh(rawRefreshToken, httpRequest.getHeader("User-Agent"),
                httpRequest.getRemoteAddr());
        setAuthCookies(httpResponse, pair.accessToken(), pair.rawRefreshToken(), pair.accessTokenTtlSeconds());

        AuthResponse body = new AuthResponse(pair.accessToken(), pair.accessTokenTtlSeconds(), pair.user());

        return ResponseEntity.ok(ApiResponse.success("Token refreshed", body));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String rawRefreshToken = extractRefreshToken(httpRequest);
        if (rawRefreshToken != null) {
            authService.logout(rawRefreshToken);
        }
        cookieGenerator.clearAuthCookies(httpResponse);
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }

     @GetMapping("/me")
     public ResponseEntity<ApiResponse<Void>> me() {
     var auth =
     org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
     return ResponseEntity.ok(ApiResponse.success((String) auth.getPrincipal()));
     }

    private void setAuthCookies(HttpServletResponse response, String accessToken, String refreshToken,
            long accessTtlSeconds) {
        cookieGenerator.setAccessTokenCookie(response, accessToken, accessTtlSeconds);
        cookieGenerator.setRefreshTokenCookie(response, refreshToken, refreshTokenTtlDays * 86400);
    }

    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new InvalidRefreshTokenException("No refresh token cookie present");
        }
        for (Cookie cookie : request.getCookies()) {
            if (refreshTokenCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new InvalidRefreshTokenException("No refresh token cookie present");
    }
}