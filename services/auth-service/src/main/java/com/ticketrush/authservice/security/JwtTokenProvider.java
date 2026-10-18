package com.ticketrush.authservice.security;

import com.ticketrush.authservice.entity.Permission;
import com.ticketrush.authservice.entity.Role;
import com.ticketrush.authservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private final JwtKeyProvider keyProvider;

    @Value("${jwt.access-token-ttl-minutes}")
    private long accessTokenTtlMinutes;

    @Value("${jwt.issuer}")
    private String issuer;

    public JwtTokenProvider(JwtKeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    public long getAccessTokenTtlSeconds() {
        return accessTokenTtlMinutes * 60;
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(getAccessTokenTtlSeconds());
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        List<String> permissions = user.getRoles().stream().flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName).distinct().toList();
                
        return Jwts.builder().setSubject(user.getId().toString()).setIssuer(issuer)
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .claim("permissions", permissions)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(keyProvider.getPrivateKey(), SignatureAlgorithm.RS256).compact();

    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyProvider.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
