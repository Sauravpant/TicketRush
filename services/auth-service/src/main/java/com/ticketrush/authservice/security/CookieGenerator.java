package com.ticketrush.authservice.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieGenerator {

    @Value("${auth.cookie.access-token-name:accessToken}")
    private String accessTokenCookieName;

    @Value("${auth.cookie.refresh-token-name:refreshToken}")
    private String refreshTokenCookieName;

    @Value("${auth.cookie.secure:false}")
    private boolean secure;

    @Value("${auth.cookie.domain:}")
    private String domain;

    public void setAccessTokenCookie(HttpServletResponse response, String token, long maxAgeSeconds) {
        addCookie(response, accessTokenCookieName, token, maxAgeSeconds, "/");
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String token, long maxAgeSeconds) {
        addCookie(response, refreshTokenCookieName, token, maxAgeSeconds, "/auth");
    }

    public void clearAuthCookies(HttpServletResponse response) {
        addCookie(response, accessTokenCookieName, "", 0, "/");
        addCookie(response, refreshTokenCookieName, "", 0, "/auth");
    }

    private void addCookie(HttpServletResponse response, String name, String value, long maxAgeSeconds, String path) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie
                .from(name, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite("Strict")
                .path(path)
                .maxAge(Duration.ofSeconds(Math.max(0, maxAgeSeconds)));

        if (domain != null && !domain.isBlank()) {
            builder.domain(domain);
        }

        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }
}