package ca.coltip.service.impl;

import ca.coltip.data.dto.RefreshTokenDTO;
import ca.coltip.data.entities.RefreshToken;
import ca.coltip.data.entities.User;
import ca.coltip.exceptions.TokenException;
import ca.coltip.data.repository.RefreshTokenRepository;
import ca.coltip.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${refresh.token.expiration}")
    private int expirationMs;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Optional<RefreshTokenDTO> findByToken(String token) throws Exception {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        return Optional.of(new RefreshTokenDTO(refreshToken.orElseThrow(() -> new TokenException("Refresh token not found"))));
    }

    public RefreshTokenDTO createRefreshToken(User user) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        RefreshToken refreshToken = existingToken.orElse(new RefreshToken());
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plusMillis(expirationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return new RefreshTokenDTO(refreshToken);
    }

    public boolean isRefreshTokenExpired(RefreshTokenDTO token) {
        if (token.getExpiresAt().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token.toEntity());
            return true;
        }

        return false;
    }
}
