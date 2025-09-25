package ca.coltip.data.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DiagnosticDTO {
    private int id;
    private String diagnosticName;
    private String description;
    private Boolean isActive;
    private Integer maxDurationMinutes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DiagnosticDomainDTO> domains;
    private List<DiagnosticResultProfileDTO> resultProfiles;

    // Constructors
    public DiagnosticDTO() {
    }

    public DiagnosticDTO(int id, String diagnosticName, String description) {
        this.id = id;
        this.diagnosticName = diagnosticName;
        this.description = description;
    }

    // Static factory method for entity conversion
    public static DiagnosticDTO fromEntity(ca.coltip.data.entities.Diagnostic diagnostic) {
        DiagnosticDTO dto = new DiagnosticDTO();
        dto.setId(diagnostic.getId());
        dto.setDiagnosticName(diagnostic.getDiagnosticName());
        dto.setDescription(diagnostic.getDescription());
        dto.setIsActive(diagnostic.getIsActive());
        dto.setMaxDurationMinutes(diagnostic.getMaxDurationMinutes());
        dto.setCreatedAt(diagnostic.getCreatedAt());
        dto.setUpdatedAt(diagnostic.getUpdatedAt());
        return dto;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<DiagnosticDomainDTO> getDomains() {
        return domains;
    }

    public void setDomains(List<DiagnosticDomainDTO> domains) {
        this.domains = domains;
    }

    public List<DiagnosticResultProfileDTO> getResultProfiles() {
        return resultProfiles;
    }

    public void setResultProfiles(List<DiagnosticResultProfileDTO> resultProfiles) {
        this.resultProfiles = resultProfiles;
    }
}