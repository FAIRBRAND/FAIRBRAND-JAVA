package ca.coltip.data.repository;

import ca.coltip.data.entities.DiagnosticDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosticDomainRepository extends JpaRepository<DiagnosticDomain, Integer> {

    @Query("SELECT d FROM DiagnosticDomain d WHERE d.diagnostic.id = :diagnosticId ORDER BY d.id")
    List<DiagnosticDomain> findByDiagnosticIdOrderById(@Param("diagnosticId") Integer diagnosticId);

    @Query("SELECT d FROM DiagnosticDomain d LEFT JOIN FETCH d.questions q LEFT JOIN FETCH q.answerOptions WHERE d.diagnostic.id = :diagnosticId ORDER BY d.id, q.id")
    List<DiagnosticDomain> findByDiagnosticIdWithQuestionsAndOptions(@Param("diagnosticId") Integer diagnosticId);

    @Query("SELECT d FROM DiagnosticDomain d WHERE d.diagnostic.id = :diagnosticId AND d.id = :domainId")
    DiagnosticDomain findByDiagnosticIdAndDomainId(@Param("diagnosticId") Integer diagnosticId,
            @Param("domainId") Integer domainId);
}