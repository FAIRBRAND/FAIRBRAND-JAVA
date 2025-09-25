package ca.coltip.data.repository;

import ca.coltip.data.entities.DiagnosticAnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosticAnswerOptionRepository extends JpaRepository<DiagnosticAnswerOption, Integer> {

    @Query("SELECT ao FROM DiagnosticAnswerOption ao WHERE ao.question.id = :questionId ORDER BY ao.id")
    List<DiagnosticAnswerOption> findByQuestionIdOrderById(@Param("questionId") Integer questionId);

    @Query("SELECT ao FROM DiagnosticAnswerOption ao WHERE ao.question.domain.id = :domainId")
    List<DiagnosticAnswerOption> findByDomainId(@Param("domainId") Integer domainId);

    @Query("SELECT MIN(ao.optionValue) FROM DiagnosticAnswerOption ao WHERE ao.question.id = :questionId")
    Double findMinValueByQuestionId(@Param("questionId") Integer questionId);

    @Query("SELECT MAX(ao.optionValue) FROM DiagnosticAnswerOption ao WHERE ao.question.id = :questionId")
    Double findMaxValueByQuestionId(@Param("questionId") Integer questionId);
}