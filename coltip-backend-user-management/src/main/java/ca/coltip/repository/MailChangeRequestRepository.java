package ca.coltip.repository;

import ca.coltip.data.entity.MailChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailChangeRequestRepository extends JpaRepository<MailChangeRequest, Long> {
}
