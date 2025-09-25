package ca.coltip.data.dto;

import ca.coltip.enums.SessionStatus;

import java.time.LocalDateTime;
import java.util.List;

public class DiagnosticSessionDTO {
    private int id;
    private String sessionToken;
    private SessionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Double totalScore;
    private Integer userId;
    private DiagnosticDTO diagnostic;
    private List<DiagnosticUserAnswerDTO> userAnswers;
    private List<DiagnosticDomainScoreDTO> domainScores;

    // Constructors
    public DiagnosticSessionDTO() {
    }

    public DiagnosticSessionDTO(int id, String sessionToken, SessionStatus status) {
        this.id = id;
        this.sessionToken = sessionToken;
        this.status = status;
    }

    // Static factory method for entity conversion
    public static DiagnosticSessionDTO fromEntity(ca.coltip.data.entities.DiagnosticSession session) {
        DiagnosticSessionDTO dto = new DiagnosticSessionDTO();
        dto.setId(session.getId());
        dto.setSessionToken(session.getSessionToken());
        dto.setStatus(session.getStatus());
        dto.setStartedAt(session.getStartedAt());
        dto.setCompletedAt(session.getCompletedAt());
        dto.setTotalScore(session.getTotalScore());
        dto.setUserId(session.getUserId());
        if (session.getDiagnostic() != null) {
            dto.setDiagnostic(DiagnosticDTO.fromEntity(session.getDiagnostic()));
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public DiagnosticDTO getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(DiagnosticDTO diagnostic) {
        this.diagnostic = diagnostic;
    }

    public List<DiagnosticUserAnswerDTO> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<DiagnosticUserAnswerDTO> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public List<DiagnosticDomainScoreDTO> getDomainScores() {
        return domainScores;
    }

    public void setDomainScores(List<DiagnosticDomainScoreDTO> domainScores) {
        this.domainScores = domainScores;
    }
}