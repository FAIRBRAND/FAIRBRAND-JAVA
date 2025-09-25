package ca.coltip.data.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@Table(name = "diagnostic_domain")
public class DiagnosticDomain {

    @Id
    @Column(name = "id_domain")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @Column(name = "domain_name", nullable = false, length = 255)
    private String domainName;

    @Column(name = "domain_code", length = 50)
    private String domainCode;

    @Column(name = "weight_factor")
    private Double weightFactor = 1.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_diagnostic", nullable = false)
    private Diagnostic diagnostic;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiagnosticQuestion> questions;

    // Constructors
    public DiagnosticDomain() {
    }

    public DiagnosticDomain(String domainName, String domainCode, Diagnostic diagnostic) {
        this.domainName = domainName;
        this.domainCode = domainCode;
        this.diagnostic = diagnostic;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public Double getWeightFactor() {
        return weightFactor;
    }

    public void setWeightFactor(Double weightFactor) {
        this.weightFactor = weightFactor;
    }

    public Diagnostic getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(Diagnostic diagnostic) {
        this.diagnostic = diagnostic;
    }

    public List<DiagnosticQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<DiagnosticQuestion> questions) {
        this.questions = questions;
    }
}