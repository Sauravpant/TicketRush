package com.ticketrush.authservice.repository;

import com.ticketrush.authservice.entity.RefreshToken;
import com.ticketrush.authservice.enums.RefreshTokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query(" UPDATE RefreshToken r SET r.status = :status WHERE r.familyId = :familyId AND r.status = 'ACTIVE' ")
    void revokeAllActiveByFamilyId(@Param("familyId") UUID familyId, @Param("status") RefreshTokenStatus status);

    @Modifying
    @Query(" UPDATE RefreshToken r SET r.status = 'REVOKED' WHERE r.userId = :userId AND r.status = 'ACTIVE' ")
    void revokeAllActiveByUserId(@Param("userId") UUID userId);
}
