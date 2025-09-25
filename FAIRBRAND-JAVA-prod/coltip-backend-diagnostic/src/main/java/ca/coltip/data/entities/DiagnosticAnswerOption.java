package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "diagnostic_answer_option")
public class DiagnosticAnswerOption {

    @Id
    @Column(name = "id_option")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @Column(name = "option_text", nullable = false, length = 500)
    private String optionText;

    @Column(name = "option_value", nullable = false)
    private Double optionValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_question", nullable = false)
    private DiagnosticQuestion question;

    // Constructors
    public DiagnosticAnswerOption() {
    }

    public DiagnosticAnswerOption(String optionText, Double optionValue, DiagnosticQuestion question) {
        this.optionText = optionText;
        this.optionValue = optionValue;
        this.question = question;
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

    public DiagnosticQuestion getQuestion() {
        return question;
    }

    public void setQuestion(DiagnosticQuestion question) {
        this.question = question;
    }
}