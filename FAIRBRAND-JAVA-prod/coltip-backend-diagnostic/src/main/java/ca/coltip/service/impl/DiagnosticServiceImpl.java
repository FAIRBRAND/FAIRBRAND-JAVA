package ca.coltip.service.impl;

import ca.coltip.data.dto.*;
import ca.coltip.data.entities.*;
import ca.coltip.data.repository.*;
import ca.coltip.enums.SessionStatus;
import ca.coltip.service.DiagnosticCalculationService;
import ca.coltip.service.DiagnosticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiagnosticServiceImpl implements DiagnosticService {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticServiceImpl.class);

    @Autowired
    private DiagnosticRepository diagnosticRepository;

    @Autowired
    private DiagnosticDomainRepository domainRepository;

    @Autowired
    private DiagnosticQuestionRepository questionRepository;

    @Autowired
    private DiagnosticAnswerOptionRepository answerOptionRepository;

    @Autowired
    private DiagnosticSessionRepository sessionRepository;

    @Autowired
    private DiagnosticUserAnswerRepository userAnswerRepository;

    @Autowired
    private DiagnosticDomainScoreRepository domainScoreRepository;

    @Autowired
    private DiagnosticCalculationService calculationService;

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticDTO> getAllActiveDiagnostics() {
        logger.info("Retrieving all active diagnostics");
        List<Diagnostic> diagnostics = diagnosticRepository.findAllActive();
        return diagnostics.stream()
                .map(DiagnosticDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DiagnosticDTO getDiagnosticById(Integer diagnosticId) {
        logger.info("Retrieving diagnostic with ID: {}", diagnosticId);
        Optional<Diagnostic> diagnostic = diagnosticRepository.findById(diagnosticId);
        if (diagnostic.isPresent()) {
            return DiagnosticDTO.fromEntity(diagnostic.get());
        }
        throw new RuntimeException("Diagnostic not found with ID: " + diagnosticId);
    }

    @Override
    @Transactional(readOnly = true)
    public DiagnosticDTO getDiagnosticWithFullDetails(Integer diagnosticId) {
        logger.info("Retrieving diagnostic with full details for ID: {}", diagnosticId);
        Optional<Diagnostic> diagnostic = diagnosticRepository.findByIdWithFullDetails(diagnosticId);
        if (diagnostic.isPresent()) {
            DiagnosticDTO dto = DiagnosticDTO.fromEntity(diagnostic.get());
            // Load domains with questions
            List<DiagnosticDomainDTO> domainDTOs = getDiagnosticDomains(diagnosticId);
            dto.setDomains(domainDTOs);
            return dto;
        }
        throw new RuntimeException("Diagnostic not found with ID: " + diagnosticId);
    }

    @Override
    public DiagnosticSessionDTO startDiagnosticSession(Integer diagnosticId, Integer userId) {
        logger.info("Starting diagnostic session for user {} and diagnostic {}", userId, diagnosticId);

        Optional<Diagnostic> diagnostic = diagnosticRepository.findById(diagnosticId);
        if (!diagnostic.isPresent()) {
            throw new RuntimeException("Diagnostic not found with ID: " + diagnosticId);
        }

        if (!diagnostic.get().getIsActive()) {
            throw new RuntimeException("Diagnostic is not active");
        }

        String sessionToken = UUID.randomUUID().toString();
        DiagnosticSession session = new DiagnosticSession(sessionToken, userId, diagnostic.get());
        session = sessionRepository.save(session);

        logger.info("Created diagnostic session with token: {}", sessionToken);
        return DiagnosticSessionDTO.fromEntity(session);
    }

    @Override
    @Transactional(readOnly = true)
    public DiagnosticSessionDTO getSessionByToken(String sessionToken) {
        logger.info("Retrieving session by token: {}", sessionToken);
        Optional<DiagnosticSession> session = sessionRepository.findBySessionToken(sessionToken);
        if (session.isPresent()) {
            return DiagnosticSessionDTO.fromEntity(session.get());
        }
        throw new RuntimeException("Session not found with token: " + sessionToken);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticSessionDTO> getUserSessions(Integer userId) {
        logger.info("Retrieving sessions for user: {}", userId);
        List<DiagnosticSession> sessions = sessionRepository.findByUserIdOrderByStartedAtDesc(userId);
        return sessions.stream()
                .map(DiagnosticSessionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public DiagnosticUserAnswerDTO saveAnswer(String sessionToken, Integer questionId, Integer selectedOptionId,
            String answerText) {
        logger.info("Saving answer for session {} and question {}", sessionToken, questionId);

        Optional<DiagnosticSession> sessionOpt = sessionRepository.findBySessionToken(sessionToken);
        if (!sessionOpt.isPresent()) {
            throw new RuntimeException("Session not found with token: " + sessionToken);
        }

        DiagnosticSession session = sessionOpt.get();
        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            throw new RuntimeException("Session is not in progress");
        }

        Optional<DiagnosticQuestion> questionOpt = questionRepository.findById(questionId);
        if (!questionOpt.isPresent()) {
            throw new RuntimeException("Question not found with ID: " + questionId);
        }

        DiagnosticQuestion question = questionOpt.get();

        // Check if answer already exists for this session and question
        Optional<DiagnosticUserAnswer> existingAnswer = userAnswerRepository
                .findBySessionIdAndQuestionId(session.getId(), questionId);

        DiagnosticUserAnswer userAnswer;
        if (existingAnswer.isPresent()) {
            userAnswer = existingAnswer.get();
            logger.info("Updating existing answer for session {} and question {}", sessionToken, questionId);
        } else {
            userAnswer = new DiagnosticUserAnswer(session, question);
            logger.info("Creating new answer for session {} and question {}", sessionToken, questionId);
        }

        // Set answer values
        if (selectedOptionId != null) {
            Optional<DiagnosticAnswerOption> optionOpt = answerOptionRepository.findById(selectedOptionId);
            if (optionOpt.isPresent()) {
                userAnswer.setSelectedOption(optionOpt.get());
                userAnswer.setNumericAnswer(optionOpt.get().getOptionValue());
            }
        }

        if (answerText != null && !answerText.trim().isEmpty()) {
            userAnswer.setTextAnswer(answerText.trim());
        }

        // Set the answered timestamp
        userAnswer.setAnsweredAt(java.time.LocalDateTime.now());

        userAnswer = userAnswerRepository.save(userAnswer);
        return DiagnosticUserAnswerDTO.fromEntity(userAnswer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticUserAnswerDTO> getSessionAnswers(String sessionToken) {
        logger.info("Retrieving answers for session: {}", sessionToken);
        List<DiagnosticUserAnswer> answers = userAnswerRepository.findBySessionToken(sessionToken);
        return answers.stream()
                .map(DiagnosticUserAnswerDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public DiagnosticResultDTO completeSession(String sessionToken) {
        logger.info("Completing session: {}", sessionToken);

        Optional<DiagnosticSession> sessionOpt = sessionRepository.findBySessionToken(sessionToken);
        if (!sessionOpt.isPresent()) {
            throw new RuntimeException("Session not found with token: " + sessionToken);
        }

        DiagnosticSession session = sessionOpt.get();
        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            throw new RuntimeException("Session is not in progress");
        }

        if (!calculationService.isSessionReadyForCalculation(session)) {
            throw new RuntimeException("Session is not ready for calculation - missing required answers");
        }

        // Calculate scores
        DiagnosticResultDTO result = calculationService.calculateSessionScores(session);

        // Update session status
        session.setStatus(SessionStatus.COMPLETED);
        session.setCompletedAt(LocalDateTime.now());
        session.setTotalScore(result.getTotalScore());
        sessionRepository.save(session);

        logger.info("Session {} completed with total score: {}", sessionToken, result.getTotalScore());
        return result;
    }

    @Override
    @Transactional
    public DiagnosticResultDTO getSessionResult(String sessionToken) {
        logger.info("Retrieving result for session: {}", sessionToken);

        // Charger d'abord la session avec les scores
        Optional<DiagnosticSession> sessionWithScoresOpt = sessionRepository.findBySessionTokenWithScores(sessionToken);
        if (!sessionWithScoresOpt.isPresent()) {
            throw new RuntimeException("Session not found with token: " + sessionToken);
        }

        DiagnosticSession session = sessionWithScoresOpt.get();
        if (session.getStatus() != SessionStatus.COMPLETED) {
            throw new RuntimeException("Session is not completed");
        }

        // Charger ensuite les réponses séparément
        Optional<DiagnosticSession> sessionWithAnswersOpt = sessionRepository
                .findBySessionTokenWithAnswers(sessionToken);
        if (sessionWithAnswersOpt.isPresent()) {
            session.setUserAnswers(sessionWithAnswersOpt.get().getUserAnswers());
        }

        return calculationService.calculateSessionScores(session);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticDomainDTO> getDiagnosticDomains(Integer diagnosticId) {
        logger.info("Retrieving domains for diagnostic: {}", diagnosticId);
        List<DiagnosticDomain> domains = domainRepository.findByDiagnosticIdOrderById(diagnosticId);
        return domains.stream()
                .map(domain -> {
                    DiagnosticDomainDTO dto = DiagnosticDomainDTO.fromEntity(domain);
                    // Load questions for each domain
                    List<DiagnosticQuestionDTO> questions = getDomainQuestions(domain.getId());
                    dto.setQuestions(questions);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticQuestionDTO> getDomainQuestions(Integer domainId) {
        logger.info("Retrieving questions for domain: {}", domainId);
        List<DiagnosticQuestion> questions = questionRepository.findByDomainIdOrderById(domainId);
        return questions.stream()
                .map(question -> {
                    DiagnosticQuestionDTO dto = DiagnosticQuestionDTO.fromEntity(question);
                    // Load answer options for each question
                    List<DiagnosticAnswerOption> options = answerOptionRepository
                            .findByQuestionIdOrderById(question.getId());
                    List<DiagnosticAnswerOptionDTO> optionDTOs = options.stream()
                            .map(DiagnosticAnswerOptionDTO::fromEntity)
                            .collect(Collectors.toList());
                    dto.setAnswerOptions(optionDTOs);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSessionValid(String sessionToken) {
        logger.debug("Checking if session is valid: {}", sessionToken);
        Optional<DiagnosticSession> session = sessionRepository.findBySessionToken(sessionToken);
        return session.isPresent() && session.get().getStatus() != SessionStatus.EXPIRED;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSessionComplete(String sessionToken) {
        logger.debug("Checking if session is complete: {}", sessionToken);
        Optional<DiagnosticSession> session = sessionRepository.findBySessionToken(sessionToken);
        return session.isPresent() && session.get().getStatus() == SessionStatus.COMPLETED;
    }

    @Override
    public void cleanupExpiredSessions() {
        logger.info("Cleaning up expired sessions");
        LocalDateTime expiredBefore = LocalDateTime.now().minusHours(24); // Sessions older than 24 hours
        List<DiagnosticSession> expiredSessions = sessionRepository.findExpiredInProgressSessions(expiredBefore);

        for (DiagnosticSession session : expiredSessions) {
            session.setStatus(SessionStatus.EXPIRED);
            sessionRepository.save(session);
        }

        logger.info("Marked {} sessions as expired", expiredSessions.size());
    }
}