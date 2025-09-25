package ca.coltip.data.repository;

import ca.coltip.data.entities.DiagnosticSession;
import ca.coltip.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosticSessionRepository extends JpaRepository<DiagnosticSession, Integer> {

    @Query("SELECT s FROM DiagnosticSession s WHERE s.sessionToken = :sessionToken")
    Optional<DiagnosticSession> findBySessionToken(@Param("sessionToken") String sessionToken);

    @Query("SELECT s FROM DiagnosticSession s WHERE s.userId = :userId ORDER BY s.startedAt DESC")
    List<DiagnosticSession> findByUserIdOrderByStartedAtDesc(@Param("userId") Integer userId);

    @Query("SELECT s FROM DiagnosticSession s WHERE s.userId = :userId AND s.status = :status")
    List<DiagnosticSession> findByUserIdAndStatus(@Param("userId") Integer userId,
            @Param("status") SessionStatus status);

    @Query("SELECT DISTINCT s FROM DiagnosticSession s LEFT JOIN FETCH s.domainScores WHERE s.sessionToken = :sessionToken")
    Optional<DiagnosticSession> findBySessionTokenWithScores(@Param("sessionToken") String sessionToken);

    @Query("SELECT DISTINCT s FROM DiagnosticSession s LEFT JOIN FETCH s.userAnswers WHERE s.sessionToken = :sessionToken")
    Optional<DiagnosticSession> findBySessionTokenWithAnswers(@Param("sessionToken") String sessionToken);

    @Query("SELECT s FROM DiagnosticSession s WHERE s.diagnostic.id = :diagnosticId AND s.status = 'COMPLETED' ORDER BY s.completedAt DESC")
    List<DiagnosticSession> findCompletedSessionsByDiagnosticId(@Param("diagnosticId") Integer diagnosticId);

    @Query("SELECT s FROM DiagnosticSession s WHERE s.status = 'IN_PROGRESS' AND s.startedAt < :expiredBefore")
    List<DiagnosticSession> findExpiredInProgressSessions(@Param("expiredBefore") LocalDateTime expiredBefore);
}