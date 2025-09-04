package ca.coltip.data.repository;

import ca.coltip.data.entities.User;
import ca.coltip.utils.RecordStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndRecordStatus(int id, int status);
    Page<User> findAllByRecordStatus(int recordStatus, Pageable pageable);
}
