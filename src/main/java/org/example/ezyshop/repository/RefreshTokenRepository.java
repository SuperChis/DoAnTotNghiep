package org.example.ezyshop.repository;

import org.example.ezyshop.entity.RefreshToken;
import org.example.ezyshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(Long userId);

    @Modifying
    int deleteByUser(User user);

    @Modifying
    void deleteRefreshTokenByUser(User user);
}
