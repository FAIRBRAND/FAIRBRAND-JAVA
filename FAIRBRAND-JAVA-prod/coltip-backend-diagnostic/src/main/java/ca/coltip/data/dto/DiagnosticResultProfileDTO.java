package ca.coltip.data.dto;

public class DiagnosticResultProfileDTO {
    private int id;
    private String profileName;
    private String profileCode;
    private String description;
    private Double minScore;
    private Double maxScore;
    private String recommendations;

    // Constructors
    public DiagnosticResultProfileDTO() {
    }

    public DiagnosticResultProfileDTO(int id, String profileName, String description) {
        this.id = id;
        this.profileName = profileName;
        this.description = description;
    }

    // Static factory method for entity conversion
    public static DiagnosticResultProfileDTO fromEntity(ca.coltip.data.entities.DiagnosticResultProfile profile) {
        DiagnosticResultProfileDTO dto = new DiagnosticResultProfileDTO();
        dto.setId(profile.getId());
        dto.setProfileName(profile.getProfileName());
        dto.setProfileCode(profile.getProfileCode());
        dto.setDescription(profile.getDescription());
        dto.setMinScore(profile.getMinScore());
        dto.setMaxScore(profile.getMaxScore());
        dto.setRecommendations(profile.getRecommendations());
        return dto;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getMinScore() {
        return minScore;
    }

    public void setMinScore(Double minScore) {
        this.minScore = minScore;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }
}