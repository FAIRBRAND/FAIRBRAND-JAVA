package ca.coltip.data.entities;

import ca.coltip.enums.SessionStatus;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "diagnostic_session")
public class DiagnosticSession {

    @Id
    @Column(name = "id_session")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @Column(name = "session_uuid", nullable = false, unique = true, length = 100)
    private String sessionToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status = SessionStatus.IN_PROGRESS;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "total_score")
    private Double totalScore;

    @Column(name = "result_profile")
    private String resultProfile;

    @Column(name = "id_user", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_diagnostic", nullable = false)
    private Diagnostic diagnostic;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiagnosticUserAnswer> userAnswers;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiagnosticDomainScore> domainScores;

    // Constructors
    public DiagnosticSession() {
    }

    public DiagnosticSession(String sessionToken, Integer userId, Diagnostic diagnostic) {
        this.sessionToken = sessionToken;
        this.userId = userId;
        this.diagnostic = diagnostic;
        this.startedAt = LocalDateTime.now();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public String getResultProfile() {
        return resultProfile;
    }

    public void setResultProfile(String resultProfile) {
        this.resultProfile = resultProfile;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Diagnostic getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(Diagnostic diagnostic) {
        this.diagnostic = diagnostic;
    }

    public List<DiagnosticUserAnswer> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<DiagnosticUserAnswer> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public List<DiagnosticDomainScore> getDomainScores() {
        return domainScores;
    }

    public void setDomainScores(List<DiagnosticDomainScore> domainScores) {
        this.domainScores = domainScores;
    }
}