package ca.coltip.data.repository;

import ca.coltip.data.entities.Diagnostic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosticRepository extends JpaRepository<Diagnostic, Integer> {

    @Query("SELECT d FROM Diagnostic d WHERE d.isActive = true")
    List<Diagnostic> findAllActive();

    @Query("SELECT d FROM Diagnostic d LEFT JOIN FETCH d.domains WHERE d.id = :id")
    Optional<Diagnostic> findByIdWithFullDetails(Integer id);

    @Query("SELECT d FROM Diagnostic d LEFT JOIN FETCH d.resultProfiles WHERE d.id = :id")
    Optional<Diagnostic> findByIdWithProfiles(Integer id);

    @Query("SELECT d FROM Diagnostic d WHERE d.isActive = true ORDER BY d.createdAt DESC")
    List<Diagnostic> findActiveOrderByCreatedDesc();
}