package ca.coltip.repository;

import ca.coltip.data.entity.RecordStatus;
import ca.coltip.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);

  <U extends User> Optional<U> findTopByEmailAndRecordStatus(String email, RecordStatus recordStatus);

  <U extends User> Optional<U> findByIdAndRecordStatus(Long id, RecordStatus recordStatus);
}
