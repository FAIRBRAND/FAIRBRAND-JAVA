package ca.coltip.data.repository;

import ca.coltip.data.entities.DiagnosticUserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosticUserAnswerRepository extends JpaRepository<DiagnosticUserAnswer, Integer> {

    @Query("SELECT ua FROM DiagnosticUserAnswer ua WHERE ua.session.id = :sessionId")
    List<DiagnosticUserAnswer> findBySessionId(@Param("sessionId") Integer sessionId);

    @Query("SELECT ua FROM DiagnosticUserAnswer ua WHERE ua.session.id = :sessionId AND ua.question.id = :questionId")
    Optional<DiagnosticUserAnswer> findBySessionIdAndQuestionId(@Param("sessionId") Integer sessionId, @Param("questionId") Integer questionId);

    @Query("SELECT ua FROM DiagnosticUserAnswer ua LEFT JOIN FETCH ua.question q LEFT JOIN FETCH ua.selectedOption ao WHERE ua.session.id = :sessionId")
    List<DiagnosticUserAnswer> findBySessionIdWithQuestionAndOption(@Param("sessionId") Integer sessionId);

    @Query("SELECT ua FROM DiagnosticUserAnswer ua WHERE ua.session.sessionToken = :sessionToken")
    List<DiagnosticUserAnswer> findBySessionToken(@Param("sessionToken") String sessionToken);

    @Query("SELECT ua FROM DiagnosticUserAnswer ua WHERE ua.question.domain.id = :domainId AND ua.session.id = :sessionId")
    List<DiagnosticUserAnswer> findByDomainIdAndSessionId(@Param("domainId") Integer domainId, @Param("sessionId") Integer sessionId);
}