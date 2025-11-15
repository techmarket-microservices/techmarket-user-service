package com.techmarket.user_service.security;

import com.techmarket.user_service.constants.MessageConstants;
import com.techmarket.user_service.exceptions.FunctionalException;
import com.techmarket.user_service.model.RefreshToken;
import com.techmarket.user_service.model.User;
import com.techmarket.user_service.repository.RefreshTokenRepository;
import com.techmarket.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshToken createRefreshToken(String email) throws FunctionalException{
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new FunctionalException(MessageConstants.USER_NOT_FOUND));
        RefreshToken token = refreshTokenRepository.findByUser(user)
                .orElse(new RefreshToken());

        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        return refreshTokenRepository.save(token);
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void delete(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }
}
