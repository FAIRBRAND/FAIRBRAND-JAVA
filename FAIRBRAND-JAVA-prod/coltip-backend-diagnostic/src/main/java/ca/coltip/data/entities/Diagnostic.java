package ca.coltip.data.entities;

import ca.coltip.data.entities.Auditable;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="diagnostic")
public class Diagnostic extends Auditable {

    @Id
    @Column(name="id_diagnostic")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @Column(name="diagnostic_name", nullable = false, length = 255)
    private String diagnosticName;

    @Column(name="description")
    private String description;

    @Column(name="is_active")
    private Boolean isActive = true;

    @Column(name="max_duration_minutes")
    private Integer maxDurationMinutes;

    @OneToMany(mappedBy = "diagnostic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiagnosticDomain> domains;

    @OneToMany(mappedBy = "diagnostic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiagnosticResultProfile> resultProfiles;

    // Constructors
    public Diagnostic() {
    }

    public Diagnostic(String diagnosticName, String description) {
        this.diagnosticName = diagnosticName;
        this.description = description;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiagnosticName() {
        return diagnosticName;
    }

    public void setDiagnosticName(String diagnosticName) {
        this.diagnosticName = diagnosticName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getMaxDurationMinutes() {
        return maxDurationMinutes;
    }

    public void setMaxDurationMinutes(Integer maxDurationMinutes) {
        this.maxDurationMinutes = maxDurationMinutes;
    }

    public List<DiagnosticDomain> getDomains() {
        return domains;
    }

    public void setDomains(List<DiagnosticDomain> domains) {
        this.domains = domains;
    }

    public List<DiagnosticResultProfile> getResultProfiles() {
        return resultProfiles;
    }

    public void setResultProfiles(List<DiagnosticResultProfile> resultProfiles) {
        this.resultProfiles = resultProfiles;
    }
}