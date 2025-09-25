package ca.coltip.data.dto;

import ca.coltip.enums.QuestionType;

import java.time.LocalDateTime;
import java.util.List;

public class DiagnosticQuestionDTO {
    private int id;
    private String questionText;
    private QuestionType questionType;
    private Boolean isRequired;
    private Integer displayOrder;
    private String helpText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DiagnosticAnswerOptionDTO> answerOptions;

    // Constructors
    public DiagnosticQuestionDTO() {
    }

    public DiagnosticQuestionDTO(int id, String questionText, QuestionType questionType) {
        this.id = id;
        this.questionText = questionText;
        this.questionType = questionType;
    }

    // Static factory method for entity conversion
    public static DiagnosticQuestionDTO fromEntity(ca.coltip.data.entities.DiagnosticQuestion question) {
        DiagnosticQuestionDTO dto = new DiagnosticQuestionDTO();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setQuestionType(question.getQuestionType());
        dto.setIsRequired(question.getIsRequired());
        dto.setDisplayOrder(question.getId()); // Use ID as order since displayOrder doesn't exist
        dto.setHelpText(null); // helpText doesn't exist in entity
        return dto;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
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

    public List<DiagnosticAnswerOptionDTO> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<DiagnosticAnswerOptionDTO> answerOptions) {
        this.answerOptions = answerOptions;
    }
}