package ca.coltip.data.dto;

import java.time.LocalDateTime;

public class DiagnosticUserAnswerDTO {
    private int id;
    private Double answerValue;
    private String answerText;
    private DiagnosticQuestionDTO question;
    private DiagnosticAnswerOptionDTO selectedOption;
    private LocalDateTime createdAt;

    // Constructors
    public DiagnosticUserAnswerDTO() {
    }

    public DiagnosticUserAnswerDTO(int id, Double answerValue, String answerText) {
        this.id = id;
        this.answerValue = answerValue;
        this.answerText = answerText;
    }

    // Static factory method for entity conversion
    public static DiagnosticUserAnswerDTO fromEntity(ca.coltip.data.entities.DiagnosticUserAnswer answer) {
        DiagnosticUserAnswerDTO dto = new DiagnosticUserAnswerDTO();
        dto.setId(answer.getId());
        dto.setAnswerValue(answer.getNumericAnswer());
        dto.setAnswerText(answer.getTextAnswer());
        dto.setCreatedAt(answer.getAnsweredAt());
        if (answer.getQuestion() != null) {
            dto.setQuestion(DiagnosticQuestionDTO.fromEntity(answer.getQuestion()));
        }
        if (answer.getSelectedOption() != null) {
            dto.setSelectedOption(DiagnosticAnswerOptionDTO.fromEntity(answer.getSelectedOption()));
        }
        return dto;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(Double answerValue) {
        this.answerValue = answerValue;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public DiagnosticQuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(DiagnosticQuestionDTO question) {
        this.question = question;
    }

    public DiagnosticAnswerOptionDTO getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(DiagnosticAnswerOptionDTO selectedOption) {
        this.selectedOption = selectedOption;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}