package com.ticketrush.authservice.service.impl;

import com.ticketrush.authservice.dto.request.*;
import com.ticketrush.authservice.dto.response.UserResponse;
import com.ticketrush.authservice.entity.*;
import com.ticketrush.authservice.enums.RefreshTokenStatus;
import com.ticketrush.authservice.enums.UserStatus;
import com.ticketrush.authservice.exception.*;
import com.ticketrush.authservice.mapper.UserMapper;
import com.ticketrush.authservice.repository.*;
import com.ticketrush.authservice.security.*;
import com.ticketrush.authservice.service.AuthService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final HashKey hashKey;
    private final UserMapper userMapper;

    @Value("${jwt.refresh-token-ttl-days}")
    private long refreshTokenTtlDays;

    private static final int MAX_FAILED_ATTEMPTS = 5;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("USER role not seeded"));

        User user = User.builder()
                .email(request.email())
                .fullName(request.fullName())
                .phoneNumber(request.phoneNumber())
                .password(passwordEncoder.encode(request.password()))
                .status(UserStatus.ACTIVE)
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        return userMapper.toResponse(user);

    }

    @Override
    @Transactional
    public TokenPair login(LoginRequest request, String userAgent, String ip) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(Instant.now())) {
            throw new AccountLockedException(user.getLockedUntil());
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            handleFailedLogin(user);
            throw new InvalidCredentialsException("Invalid email or password");
        }

        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
        userRepository.save(user);

        return issueTokenPair(user, UUID.randomUUID(), userAgent, ip);

    }

    @Override
    @Transactional
    public TokenPair refresh(String rawRefreshToken, String userAgent, String ip) {
        String incomingHash = hashKey.sha256(rawRefreshToken);

        RefreshToken current = refreshTokenRepository.findByTokenHash(incomingHash)
                .orElseThrow(() -> new InvalidRefreshTokenException("Unknown refresh token"));

        if (current.getStatus() == RefreshTokenStatus.ROTATED) {
            refreshTokenRepository.revokeAllActiveByFamilyId(current.getFamilyId(), RefreshTokenStatus.REVOKED);
            throw new InvalidRefreshTokenException("Token reused");
        }

        if (current.getStatus() == RefreshTokenStatus.REVOKED || current.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidRefreshTokenException("Refresh token expired or revoked");
        }

        User user = userRepository.findById(current.getUserId())
                .orElseThrow(() -> new InvalidRefreshTokenException("User no longer exists"));

        TokenPair newPair = issueTokenPair(user, current.getFamilyId(), userAgent, ip);

        current.setStatus(RefreshTokenStatus.ROTATED);
        current.setRotatedAt(Instant.now());
        refreshTokenRepository.save(current);

        RefreshToken newToken = refreshTokenRepository.findByTokenHash(hashKey.sha256(newPair.rawRefreshToken()))
                .orElseThrow(() -> new IllegalStateException("Just created refresh token not found"));
        current.setReplacedById(newToken.getId());

        refreshTokenRepository.save(current);

        return newPair;

    }

    @Override
    @Transactional
    public void logout(String rawRefreshToken) {
        String hash = hashKey.sha256(rawRefreshToken);
        refreshTokenRepository.findByTokenHash(hash).ifPresent(token -> {
            token.setStatus(RefreshTokenStatus.REVOKED);
            refreshTokenRepository.save(token);
        });
    }

    @Override
    @Transactional
    public void logoutAllDevices(UUID userId) {
        refreshTokenRepository.revokeAllActiveByUserId(userId);
    }

    private TokenPair issueTokenPair(User user, UUID familyId, String userAgent, String ip) {
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String rawRefreshToken = refreshTokenGenerator.generate();

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .familyId(familyId)
                .tokenHash(hashKey.sha256(rawRefreshToken))
                .status(RefreshTokenStatus.ACTIVE)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(refreshTokenTtlDays * 86400))
                .userAgent(userAgent)
                .ip(ip)
                .build();

        refreshTokenRepository.save(refreshToken);

        return new TokenPair(accessToken, rawRefreshToken, jwtTokenProvider.getAccessTokenTtlSeconds(),
                userMapper.toResponse(user));
    }

    private void handleFailedLogin(User user) {
        int attempts = user.getFailedLoginCount() + 1;
        user.setFailedLoginCount(attempts);
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setLockedUntil(Instant.now().plusSeconds(900));
        }
        userRepository.save(user);
    }

}
