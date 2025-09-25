package ca.coltip.service;

import ca.coltip.data.dto.DiagnosticDomainScoreDTO;
import ca.coltip.data.dto.DiagnosticResultDTO;
import ca.coltip.data.entities.DiagnosticSession;

import java.util.List;

public interface DiagnosticCalculationService {

    // Score calculation methods
    DiagnosticResultDTO calculateSessionScores(DiagnosticSession session);
    List<DiagnosticDomainScoreDTO> calculateDomainScores(DiagnosticSession session);
    Double calculateTotalScore(List<DiagnosticDomainScoreDTO> domainScores);
    Double calculatePercentageScore(Double totalScore, Double maxPossibleScore);

    // Profile matching
    DiagnosticResultDTO determineResultProfile(DiagnosticSession session, Double totalScore);

    // Validation methods
    boolean isSessionReadyForCalculation(DiagnosticSession session);
    Double getMaxPossibleScore(Integer diagnosticId);

    // Statistical methods
    Double getAverageDomainScore(Integer domainId);
    List<DiagnosticDomainScoreDTO> getComparisonScores(Integer diagnosticId);
}