package ca.coltip.service;

import ca.coltip.data.dto.*;
import ca.coltip.data.entities.DiagnosticSession;

import java.util.List;

public interface DiagnosticService {

    // Diagnostic management
    List<DiagnosticDTO> getAllActiveDiagnostics();
    DiagnosticDTO getDiagnosticById(Integer diagnosticId);
    DiagnosticDTO getDiagnosticWithFullDetails(Integer diagnosticId);

    // Session management
    DiagnosticSessionDTO startDiagnosticSession(Integer diagnosticId, Integer userId);
    DiagnosticSessionDTO getSessionByToken(String sessionToken);
    List<DiagnosticSessionDTO> getUserSessions(Integer userId);

    // Answer management
    DiagnosticUserAnswerDTO saveAnswer(String sessionToken, Integer questionId, Integer selectedOptionId, String answerText);
    List<DiagnosticUserAnswerDTO> getSessionAnswers(String sessionToken);

    // Score calculation and completion
    DiagnosticResultDTO completeSession(String sessionToken);
    DiagnosticResultDTO getSessionResult(String sessionToken);

    // Domain and question queries
    List<DiagnosticDomainDTO> getDiagnosticDomains(Integer diagnosticId);
    List<DiagnosticQuestionDTO> getDomainQuestions(Integer domainId);

    // Session validation
    boolean isSessionValid(String sessionToken);
    boolean isSessionComplete(String sessionToken);

    // Cleanup expired sessions
    void cleanupExpiredSessions();
}