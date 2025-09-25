package ca.coltip.data.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DiagnosticDomainDTO {
    private int id;
    private String domainName;
    private String description;
    private Double weight;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DiagnosticQuestionDTO> questions;

    // Constructors
    public DiagnosticDomainDTO() {
    }

    public DiagnosticDomainDTO(int id, String domainName, String description) {
        this.id = id;
        this.domainName = domainName;
        this.description = description;
    }

    // Static factory method for entity conversion
    public static DiagnosticDomainDTO fromEntity(ca.coltip.data.entities.DiagnosticDomain domain) {
        DiagnosticDomainDTO dto = new DiagnosticDomainDTO();
        dto.setId(domain.getId());
        dto.setDomainName(domain.getDomainName());
        dto.setDescription(domain.getDomainCode()); // Using domainCode as description for now
        dto.setWeight(domain.getWeightFactor());
        dto.setDisplayOrder(null); // This field doesn't exist in entity anymore
        dto.setCreatedAt(null); // This field doesn't exist in entity anymore
        dto.setUpdatedAt(null); // This field doesn't exist in entity anymore
        return dto;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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

    public List<DiagnosticQuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<DiagnosticQuestionDTO> questions) {
        this.questions = questions;
    }
}