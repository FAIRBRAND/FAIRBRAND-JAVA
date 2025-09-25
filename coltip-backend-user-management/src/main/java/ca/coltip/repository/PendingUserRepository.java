package ca.coltip.repository;

import ca.coltip.data.entity.PendingUserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PendingUserRepository extends JpaRepository<PendingUserRegistration, Long> {
  boolean existsByEmail(String email);

  Optional<PendingUserRegistration> findTopByEmailOrderByExpiresAtDesc(String email);

  @Modifying
  @Transactional
  void deleteAllByEmail(String email);
}
