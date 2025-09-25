package ca.coltip.data.repository;

import ca.coltip.data.entities.DiagnosticDomainScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosticDomainScoreRepository extends JpaRepository<DiagnosticDomainScore, Integer> {

    @Query("SELECT ds FROM DiagnosticDomainScore ds WHERE ds.session.id = :sessionId")
    List<DiagnosticDomainScore> findBySessionId(@Param("sessionId") Integer sessionId);

    @Query("SELECT ds FROM DiagnosticDomainScore ds LEFT JOIN FETCH ds.domain d WHERE ds.session.id = :sessionId ORDER BY d.id")
    List<DiagnosticDomainScore> findBySessionIdWithDomainOrderById(@Param("sessionId") Integer sessionId);

    @Query("SELECT ds FROM DiagnosticDomainScore ds WHERE ds.session.id = :sessionId AND ds.domain.id = :domainId")
    Optional<DiagnosticDomainScore> findBySessionIdAndDomainId(@Param("sessionId") Integer sessionId,
            @Param("domainId") Integer domainId);

    @Query("SELECT ds FROM DiagnosticDomainScore ds WHERE ds.session.sessionToken = :sessionToken")
    List<DiagnosticDomainScore> findBySessionToken(@Param("sessionToken") String sessionToken);

    @Query("SELECT AVG(ds.percentageScore) FROM DiagnosticDomainScore ds WHERE ds.domain.id = :domainId")
    Double findAveragePercentageScoreByDomainId(@Param("domainId") Integer domainId);
}