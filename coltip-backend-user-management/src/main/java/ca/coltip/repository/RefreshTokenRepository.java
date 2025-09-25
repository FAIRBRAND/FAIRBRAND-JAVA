package ca.coltip.repository;

import ca.coltip.data.entity.RefreshToken;
import ca.coltip.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findTopByUser(User user);

  Optional<RefreshToken> findTopByToken(String token);
}
