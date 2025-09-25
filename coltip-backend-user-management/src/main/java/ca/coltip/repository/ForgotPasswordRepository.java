package ca.coltip.repository;

import ca.coltip.data.entity.ForgotPasswordRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordRequest, Long> {
  <F extends ForgotPasswordRequest> Optional<F> findTopByEmailOrderByCreatedAtDesc(String email);
}
