package ca.coltip.data.repository;

import ca.coltip.data.entities.PendingUserRegistration;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PendingUserRegistrationRepository extends JpaRepository<PendingUserRegistration, Integer>  {
    Optional<PendingUserRegistration> findTopByEmailOrderByExpiresAtDesc(String email);
    @Modifying
    @Transactional
    void deleteAllByEmail(String email);
}
