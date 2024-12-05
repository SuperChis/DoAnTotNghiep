package org.example.ezyshop.service;

import org.example.ezyshop.config.jwt.JwtUtils;
import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.dto.refreshToken.RefreshTokenRequest;
import org.example.ezyshop.dto.refreshToken.RefreshTokenResponse;
import org.example.ezyshop.entity.RefreshToken;
import org.example.ezyshop.exception.TokenRefreshException;
import org.example.ezyshop.repository.RefreshTokenRepository;
import org.example.ezyshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${EzyShop.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    public RefreshTokenResponse refreshTokenForUser(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return this.findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return new RefreshTokenResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }


    public RefreshToken createRefreshToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId();

        // Kiểm tra Refresh Token đã tồn tại
        Optional<RefreshToken> existingToken = repository.findByUserId(userId);

        if (existingToken.isPresent()) {
            RefreshToken refreshToken = existingToken.get();
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());
            return repository.save(refreshToken);
        }

        // Nếu chưa tồn tại, tạo mới
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found: " + userId)));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        return repository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            repository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return repository.deleteByUser(userRepository.findById(userId).get());
    }
}
