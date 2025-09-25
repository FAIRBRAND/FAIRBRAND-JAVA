package ca.coltip.data.repository;

import ca.coltip.data.entities.DiagnosticQuestion;
import ca.coltip.enums.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosticQuestionRepository extends JpaRepository<DiagnosticQuestion, Integer> {

    @Query("SELECT q FROM DiagnosticQuestion q WHERE q.domain.id = :domainId ORDER BY q.id")
    List<DiagnosticQuestion> findByDomainIdOrderById(@Param("domainId") Integer domainId);

    @Query("SELECT q FROM DiagnosticQuestion q LEFT JOIN FETCH q.answerOptions ao WHERE q.id = :questionId ORDER BY ao.id")
    Optional<DiagnosticQuestion> findByIdWithAnswerOptions(@Param("questionId") Integer questionId);

    @Query("SELECT q FROM DiagnosticQuestion q WHERE q.domain.diagnostic.id = :diagnosticId AND q.questionType = :questionType")
    List<DiagnosticQuestion> findByDiagnosticIdAndQuestionType(@Param("diagnosticId") Integer diagnosticId,
            @Param("questionType") QuestionType questionType);

    @Query("SELECT q FROM DiagnosticQuestion q WHERE q.domain.diagnostic.id = :diagnosticId AND q.isRequired = true")
    List<DiagnosticQuestion> findRequiredQuestionsByDiagnosticId(@Param("diagnosticId") Integer diagnosticId);
}