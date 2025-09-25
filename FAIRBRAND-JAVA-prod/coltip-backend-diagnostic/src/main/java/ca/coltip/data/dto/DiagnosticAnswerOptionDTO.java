package ca.coltip.data.dto;

import java.time.LocalDateTime;

public class DiagnosticAnswerOptionDTO {
    private int id;
    private String optionText;
    private Double optionValue;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public DiagnosticAnswerOptionDTO() {
    }

    public DiagnosticAnswerOptionDTO(int id, String optionText, Double optionValue) {
        this.id = id;
        this.optionText = optionText;
        this.optionValue = optionValue;
    }

    // Static factory method for entity conversion
    public static DiagnosticAnswerOptionDTO fromEntity(ca.coltip.data.entities.DiagnosticAnswerOption option) {
        DiagnosticAnswerOptionDTO dto = new DiagnosticAnswerOptionDTO();
        dto.setId(option.getId());
        dto.setOptionText(option.getOptionText());
        dto.setOptionValue(option.getOptionValue());
        dto.setDisplayOrder(option.getId()); // Use ID as order since displayOrder doesn't exist
        return dto;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Double getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(Double optionValue) {
        this.optionValue = optionValue;
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
}