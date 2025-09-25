package ca.coltip.data.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DiagnosticResultDTO {
    private int sessionId;
    private String sessionToken;
    private Integer userId;
    private String diagnosticName;
    private LocalDateTime completedAt;
    private Double totalScore;
    private Double percentageScore;
    private List<DiagnosticDomainScoreDTO> domainScores;
    private DiagnosticResultProfileDTO resultProfile;
    private String recommendations;

    // Constructors
    public DiagnosticResultDTO() {
    }

    public DiagnosticResultDTO(int sessionId, Double totalScore, String diagnosticName) {
        this.sessionId = sessionId;
        this.totalScore = totalScore;
        this.diagnosticName = diagnosticName;
    }

    // Getters and setters
    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDiagnosticName() {
        return diagnosticName;
    }

    public void setDiagnosticName(String diagnosticName) {
        this.diagnosticName = diagnosticName;
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

    public Double getPercentageScore() {
        return percentageScore;
    }

    public void setPercentageScore(Double percentageScore) {
        this.percentageScore = percentageScore;
    }

    public List<DiagnosticDomainScoreDTO> getDomainScores() {
        return domainScores;
    }

    public void setDomainScores(List<DiagnosticDomainScoreDTO> domainScores) {
        this.domainScores = domainScores;
    }

    public DiagnosticResultProfileDTO getResultProfile() {
        return resultProfile;
    }

    public void setResultProfile(DiagnosticResultProfileDTO resultProfile) {
        this.resultProfile = resultProfile;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }
}