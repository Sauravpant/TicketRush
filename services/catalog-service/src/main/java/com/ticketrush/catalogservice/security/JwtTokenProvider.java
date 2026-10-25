package com.ticketrush.catalogservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final JwtKeyProvider keyProvider;

    public JwtTokenProvider(JwtKeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyProvider.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}