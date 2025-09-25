package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnostic_user_answer")
public class DiagnosticUserAnswer {

    @Id
    @Column(name = "id_answer")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @Column(name = "numeric_answer")
    private Double numericAnswer;

    @Column(name = "text_answer", columnDefinition = "TEXT")
    private String textAnswer;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_session", nullable = false)
    private DiagnosticSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_question", nullable = false)
    private DiagnosticQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_option")
    private DiagnosticAnswerOption selectedOption;

    // Constructors
    public DiagnosticUserAnswer() {
    }

    public DiagnosticUserAnswer(DiagnosticSession session, DiagnosticQuestion question) {
        this.session = session;
        this.question = question;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getNumericAnswer() {
        return numericAnswer;
    }

    public void setNumericAnswer(Double numericAnswer) {
        this.numericAnswer = numericAnswer;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(LocalDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }

    public DiagnosticSession getSession() {
        return session;
    }

    public void setSession(DiagnosticSession session) {
        this.session = session;
    }

    public DiagnosticQuestion getQuestion() {
        return question;
    }

    public void setQuestion(DiagnosticQuestion question) {
        this.question = question;
    }

    public DiagnosticAnswerOption getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(DiagnosticAnswerOption selectedOption) {
        this.selectedOption = selectedOption;
    }
}