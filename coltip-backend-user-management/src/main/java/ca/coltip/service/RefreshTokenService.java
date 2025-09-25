package ca.coltip.service;

import ca.coltip.data.entity.RefreshToken;
import ca.coltip.data.entity.User;
import ca.coltip.exception.TokenNotFoundException;
import ca.coltip.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {
  private final Environment env;
  private final RefreshTokenRepository repository;

  public RefreshToken getByToken(String token) throws TokenNotFoundException {
    return repository
      .findTopByToken(token)
      .orElseThrow(TokenNotFoundException::new);
  }

  private Instant createExpiration() {
    final var expirationMs = env.getRequiredProperty("jwt.expiration", Long.class);
    return Instant.now().plusMillis(expirationMs);
  }

  public RefreshToken createRefreshToken(User user) {
    final var refreshToken = repository
      .findTopByUser(user)
      .orElseGet(RefreshToken::new);

    refreshToken.setUser(user);
    refreshToken.setExpiresAt(createExpiration());
    refreshToken.setToken(UUID.randomUUID().toString());

    return repository.save(refreshToken);
  }

  public void delete(RefreshToken refreshToken) {
    repository.delete(refreshToken);
  }
}
