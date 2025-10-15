package ca.coltip.repository;

import ca.coltip.data.entity.RecordStatus;
import ca.coltip.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);

  <U extends User> Optional<U> findTopByEmailAndRecordStatus(String email, RecordStatus recordStatus);

  <U extends User> Optional<U> findByIdAndRecordStatus(Long id, RecordStatus recordStatus);

  @Query(
    value = "SELECT COUNT(u.id_user) FROM \"users\" u WHERE u.record_status = :status AND EXTRACT(YEAR FROM u.created_at) = :year AND EXTRACT(MONTH FROM u.created_at) = :month",
    nativeQuery = true
  )
  Long countUsersByMonthAndYearAndStatus(
    @Param("month") int month,
    @Param("year") int year,
    @Param("status") int status
  );

  Long countUserByRecordStatus(RecordStatus status);
}
