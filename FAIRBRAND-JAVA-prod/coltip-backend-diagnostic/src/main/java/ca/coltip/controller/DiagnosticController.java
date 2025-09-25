package ca.coltip.controller;

import ca.coltip.data.dto.*;
import ca.coltip.service.DiagnosticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diagnostic")
@CrossOrigin(origins = "*")
public class DiagnosticController {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticController.class);

    @Autowired
    private DiagnosticService diagnosticService;

    /**
     * Get all active diagnostics
     */
    @GetMapping("/diagnostics")
    public ResponseEntity<List<DiagnosticDTO>> getAllActiveDiagnostics() {
        try {
            logger.info("Request to get all active diagnostics");
            List<DiagnosticDTO> diagnostics = diagnosticService.getAllActiveDiagnostics();
            return ResponseEntity.ok(diagnostics);
        } catch (Exception e) {
            logger.error("Error retrieving active diagnostics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get diagnostic by ID with full details (domains, questions, options)
     */
    @GetMapping("/diagnostics/{diagnosticId}")
    public ResponseEntity<DiagnosticDTO> getDiagnosticWithDetails(@PathVariable Integer diagnosticId) {
        try {
            logger.info("Request to get diagnostic details for ID: {}", diagnosticId);
            DiagnosticDTO diagnostic = diagnosticService.getDiagnosticWithFullDetails(diagnosticId);
            return ResponseEntity.ok(diagnostic);
        } catch (RuntimeException e) {
            logger.error("Error retrieving diagnostic with ID: {}", diagnosticId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Unexpected error retrieving diagnostic", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Start a new diagnostic session
     */
    @PostMapping("/sessions/start")
    public ResponseEntity<DiagnosticSessionDTO> startSession(@RequestBody Map<String, Object> request) {
        try {
            Integer diagnosticId = (Integer) request.get("diagnosticId");
            Integer userId = (Integer) request.get("userId");

            logger.info("Request to start session for user {} and diagnostic {}", userId, diagnosticId);
            
            if (diagnosticId == null || userId == null) {
                return ResponseEntity.badRequest().build();
            }

            DiagnosticSessionDTO session = diagnosticService.startDiagnosticSession(diagnosticId, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(session);
        } catch (RuntimeException e) {
            logger.error("Error starting diagnostic session", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error starting session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get session information by token
     */
    @GetMapping("/sessions/{sessionToken}")
    public ResponseEntity<DiagnosticSessionDTO> getSession(@PathVariable String sessionToken) {
        try {
            logger.info("Request to get session: {}", sessionToken);
            DiagnosticSessionDTO session = diagnosticService.getSessionByToken(sessionToken);
            return ResponseEntity.ok(session);
        } catch (RuntimeException e) {
            logger.error("Error retrieving session: {}", sessionToken, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Unexpected error retrieving session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all sessions for a user
     */
    @GetMapping("/users/{userId}/sessions")
    public ResponseEntity<List<DiagnosticSessionDTO>> getUserSessions(@PathVariable Integer userId) {
        try {
            logger.info("Request to get sessions for user: {}", userId);
            List<DiagnosticSessionDTO> sessions = diagnosticService.getUserSessions(userId);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            logger.error("Error retrieving sessions for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Save an answer to a question
     */
    @PostMapping("/sessions/{sessionToken}/answers")
    public ResponseEntity<DiagnosticUserAnswerDTO> saveAnswer(
            @PathVariable String sessionToken,
            @RequestBody Map<String, Object> answerData) {
        try {
            Integer questionId = (Integer) answerData.get("questionId");
            Integer selectedOptionId = (Integer) answerData.get("selectedOptionId");
            String answerText = (String) answerData.get("answerText");

            logger.info("Request to save answer for session {} and question {}", sessionToken, questionId);

            if (questionId == null) {
                return ResponseEntity.badRequest().build();
            }

            DiagnosticUserAnswerDTO answer = diagnosticService.saveAnswer(
                    sessionToken, questionId, selectedOptionId, answerText);
            return ResponseEntity.ok(answer);
        } catch (RuntimeException e) {
            logger.error("Error saving answer for session: {}", sessionToken, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error saving answer", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all answers for a session
     */
    @GetMapping("/sessions/{sessionToken}/answers")
    public ResponseEntity<List<DiagnosticUserAnswerDTO>> getSessionAnswers(@PathVariable String sessionToken) {
        try {
            logger.info("Request to get answers for session: {}", sessionToken);
            List<DiagnosticUserAnswerDTO> answers = diagnosticService.getSessionAnswers(sessionToken);
            return ResponseEntity.ok(answers);
        } catch (Exception e) {
            logger.error("Error retrieving answers for session: {}", sessionToken, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Complete a diagnostic session and calculate results
     */
    @PostMapping("/sessions/{sessionToken}/complete")
    public ResponseEntity<DiagnosticResultDTO> completeSession(@PathVariable String sessionToken) {
        try {
            logger.info("Request to complete session: {}", sessionToken);
            DiagnosticResultDTO result = diagnosticService.completeSession(sessionToken);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            logger.error("Error completing session: {}", sessionToken, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error completing session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get results for a completed session
     */
    @GetMapping("/sessions/{sessionToken}/results")
    public ResponseEntity<DiagnosticResultDTO> getSessionResults(@PathVariable String sessionToken) {
        try {
            logger.info("Request to get results for session: {}", sessionToken);
            DiagnosticResultDTO result = diagnosticService.getSessionResult(sessionToken);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            logger.error("Error retrieving results for session: {}", sessionToken, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Unexpected error retrieving results", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get domains for a diagnostic
     */
    @GetMapping("/diagnostics/{diagnosticId}/domains")
    public ResponseEntity<List<DiagnosticDomainDTO>> getDiagnosticDomains(@PathVariable Integer diagnosticId) {
        try {
            logger.info("Request to get domains for diagnostic: {}", diagnosticId);
            List<DiagnosticDomainDTO> domains = diagnosticService.getDiagnosticDomains(diagnosticId);
            return ResponseEntity.ok(domains);
        } catch (Exception e) {
            logger.error("Error retrieving domains for diagnostic: {}", diagnosticId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get questions for a domain
     */
    @GetMapping("/domains/{domainId}/questions")
    public ResponseEntity<List<DiagnosticQuestionDTO>> getDomainQuestions(@PathVariable Integer domainId) {
        try {
            logger.info("Request to get questions for domain: {}", domainId);
            List<DiagnosticQuestionDTO> questions = diagnosticService.getDomainQuestions(domainId);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            logger.error("Error retrieving questions for domain: {}", domainId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Check if session is valid and active
     */
    @GetMapping("/sessions/{sessionToken}/validate")
    public ResponseEntity<Map<String, Object>> validateSession(@PathVariable String sessionToken) {
        try {
            logger.info("Request to validate session: {}", sessionToken);
            boolean isValid = diagnosticService.isSessionValid(sessionToken);
            boolean isComplete = diagnosticService.isSessionComplete(sessionToken);
            
            Map<String, Object> response = Map.of(
                "isValid", isValid,
                "isComplete", isComplete
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error validating session: {}", sessionToken, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cleanup expired sessions (admin endpoint)
     */
    @PostMapping("/admin/cleanup-expired")
    public ResponseEntity<Map<String, String>> cleanupExpiredSessions() {
        try {
            logger.info("Request to cleanup expired sessions");
            diagnosticService.cleanupExpiredSessions();
            return ResponseEntity.ok(Map.of("message", "Expired sessions cleaned up successfully"));
        } catch (Exception e) {
            logger.error("Error cleaning up expired sessions", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to cleanup expired sessions"));
        }
    }
}