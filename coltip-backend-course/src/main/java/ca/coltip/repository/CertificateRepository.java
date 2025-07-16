package ca.coltip.repository;

import ca.coltip.data.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Integer> {
    @Query("SELECT c FROM Certificate c WHERE c.user.id = :userId AND c.course.id = :courseId")
    Optional<Certificate> findByUserIdAndCourseId(@Param("userId") int userId,
                                                  @Param("courseId") int courseId);

    @Query("SELECT c FROM Certificate c WHERE c.user.id = :userId AND c.recordStatus = :status")
    List<Certificate> findAllByUserIdAndRecordStatus(@Param("userId") int userId,
                                                     @Param("status") int status);

    @Query("SELECT c FROM Certificate c WHERE c.id = :id AND c.recordStatus = :status")
    Optional<Certificate> findByIdAndRecordStatus(@Param("id") int id,
                                                  @Param("status") int status);
}
