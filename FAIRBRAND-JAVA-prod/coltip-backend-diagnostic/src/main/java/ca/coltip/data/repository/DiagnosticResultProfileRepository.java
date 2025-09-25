package ca.coltip.data.repository;

import ca.coltip.data.entities.DiagnosticResultProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosticResultProfileRepository extends JpaRepository<DiagnosticResultProfile, Integer> {

    @Query("SELECT rp FROM DiagnosticResultProfile rp WHERE rp.diagnostic.id = :diagnosticId ORDER BY rp.minScore")
    List<DiagnosticResultProfile> findByDiagnosticIdOrderByMinScore(@Param("diagnosticId") Integer diagnosticId);

    @Query("SELECT rp FROM DiagnosticResultProfile rp WHERE rp.diagnostic.id = :diagnosticId AND :score BETWEEN rp.minScore AND rp.maxScore")
    Optional<DiagnosticResultProfile> findByDiagnosticIdAndScoreRange(@Param("diagnosticId") Integer diagnosticId, @Param("score") Double score);

    @Query("SELECT rp FROM DiagnosticResultProfile rp WHERE rp.diagnostic.id = :diagnosticId AND rp.minScore <= :score AND (rp.maxScore IS NULL OR rp.maxScore >= :score)")
    List<DiagnosticResultProfile> findMatchingProfilesByScore(@Param("diagnosticId") Integer diagnosticId, @Param("score") Double score);
}