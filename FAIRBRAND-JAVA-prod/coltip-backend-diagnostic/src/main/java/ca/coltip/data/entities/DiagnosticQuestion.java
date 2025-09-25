package ca.coltip.data.entities;

import ca.coltip.enums.QuestionType;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@Table(name = "diagnostic_question")
public class DiagnosticQuestion {

    @Id
    @Column(name = "id_question")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @Column(name = "is_required")
    private Boolean isRequired = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_domain", nullable = false)
    private DiagnosticDomain domain;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiagnosticAnswerOption> answerOptions;

    // Constructors
    public DiagnosticQuestion() {
    }

    public DiagnosticQuestion(String questionText, QuestionType questionType, DiagnosticDomain domain) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.domain = domain;
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

    public DiagnosticDomain getDomain() {
        return domain;
    }

    public void setDomain(DiagnosticDomain domain) {
        this.domain = domain;
    }

    public List<DiagnosticAnswerOption> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<DiagnosticAnswerOption> answerOptions) {
        this.answerOptions = answerOptions;
    }
}