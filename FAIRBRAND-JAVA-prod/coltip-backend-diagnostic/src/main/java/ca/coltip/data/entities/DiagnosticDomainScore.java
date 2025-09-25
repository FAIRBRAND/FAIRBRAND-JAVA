package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "diagnostic_domain_score")
public class DiagnosticDomainScore {

    @Id
    @Column(name = "id_domain_score")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @Column(name = "raw_score", nullable = false)
    private Double rawScore;

    @Column(name = "weighted_score")
    private Double weightedScore;

    @Column(name = "percentage")
    private Double percentageScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_session", nullable = false)
    private DiagnosticSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_domain", nullable = false)
    private DiagnosticDomain domain;

    // Constructors
    public DiagnosticDomainScore() {
    }

    public DiagnosticDomainScore(DiagnosticSession session, DiagnosticDomain domain, Double rawScore) {
        this.session = session;
        this.domain = domain;
        this.rawScore = rawScore;
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

    public DiagnosticSession getSession() {
        return session;
    }

    public void setSession(DiagnosticSession session) {
        this.session = session;
    }

    public DiagnosticDomain getDomain() {
        return domain;
    }

    public void setDomain(DiagnosticDomain domain) {
        this.domain = domain;
    }
}