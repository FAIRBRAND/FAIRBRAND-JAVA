package ca.coltip.data.dto;

public class DiagnosticDomainScoreDTO {
    private int id;
    private Double rawScore;
    private Double weightedScore;
    private Double percentageScore;
    private DiagnosticDomainDTO domain;

    // Constructors
    public DiagnosticDomainScoreDTO() {
    }

    public DiagnosticDomainScoreDTO(int id, Double rawScore, Double weightedScore) {
        this.id = id;
        this.rawScore = rawScore;
        this.weightedScore = weightedScore;
    }

    // Static factory method for entity conversion
    public static DiagnosticDomainScoreDTO fromEntity(ca.coltip.data.entities.DiagnosticDomainScore score) {
        DiagnosticDomainScoreDTO dto = new DiagnosticDomainScoreDTO();
        dto.setId(score.getId());
        dto.setRawScore(score.getRawScore());
        dto.setWeightedScore(score.getWeightedScore());
        dto.setPercentageScore(score.getPercentageScore());
        if (score.getDomain() != null) {
            dto.setDomain(DiagnosticDomainDTO.fromEntity(score.getDomain()));
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

    public Double getRawScore() {
        return rawScore;
    }

    public void setRawScore(Double rawScore) {
        this.rawScore = rawScore;
    }

    public Double getWeightedScore() {
        return weightedScore;
    }

    public void setWeightedScore(Double weightedScore) {
        this.weightedScore = weightedScore;
    }

    public Double getPercentageScore() {
        return percentageScore;
    }

    public void setPercentageScore(Double percentageScore) {
        this.percentageScore = percentageScore;
    }

    public DiagnosticDomainDTO getDomain() {
        return domain;
    }

    public void setDomain(DiagnosticDomainDTO domain) {
        this.domain = domain;
    }
}