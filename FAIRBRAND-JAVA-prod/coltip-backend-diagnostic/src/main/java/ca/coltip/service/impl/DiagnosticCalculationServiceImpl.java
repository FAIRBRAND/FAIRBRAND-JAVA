package ca.coltip.service.impl;

import ca.coltip.data.dto.DiagnosticDomainScoreDTO;
import ca.coltip.data.dto.DiagnosticResultDTO;
import ca.coltip.data.dto.DiagnosticResultProfileDTO;
import ca.coltip.data.entities.*;
import ca.coltip.data.repository.*;
import ca.coltip.service.DiagnosticCalculationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiagnosticCalculationServiceImpl implements DiagnosticCalculationService {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticCalculationServiceImpl.class);

    @Autowired
    private DiagnosticUserAnswerRepository userAnswerRepository;

    @Autowired
    private DiagnosticDomainScoreRepository domainScoreRepository;

    @Autowired
    private DiagnosticDomainRepository domainRepository;

    @Autowired
    private DiagnosticQuestionRepository questionRepository;

    @Autowired
    private DiagnosticAnswerOptionRepository answerOptionRepository;

    @Autowired
    private DiagnosticResultProfileRepository resultProfileRepository;

    @Override
    public DiagnosticResultDTO calculateSessionScores(DiagnosticSession session) {
        logger.info("Calculating scores for session: {}", session.getSessionToken());

        if (!isSessionReadyForCalculation(session)) {
            throw new RuntimeException("Session is not ready for score calculation");
        }

        // Calculate domain scores
        List<DiagnosticDomainScoreDTO> domainScores = calculateDomainScores(session);

        // Calculate total score
        Double totalScore = calculateTotalScore(domainScores);

        // Calculate percentage score
        Double maxPossibleScore = getMaxPossibleScore(session.getDiagnostic().getId());
        Double percentageScore = calculatePercentageScore(totalScore, maxPossibleScore);

        // Create result DTO
        DiagnosticResultDTO result = new DiagnosticResultDTO();
        result.setSessionId(session.getId());
        result.setSessionToken(session.getSessionToken());
        result.setUserId(session.getUserId());
        result.setDiagnosticName(session.getDiagnostic().getDiagnosticName());
        result.setTotalScore(totalScore);
        result.setPercentageScore(percentageScore);
        result.setDomainScores(domainScores);
        result.setCompletedAt(LocalDateTime.now());

        // Determine result profile
        Optional<DiagnosticResultProfile> profileOpt = resultProfileRepository
                .findByDiagnosticIdAndScoreRange(session.getDiagnostic().getId(), percentageScore);

        if (profileOpt.isPresent()) {
            DiagnosticResultProfile profile = profileOpt.get();
            result.setResultProfile(DiagnosticResultProfileDTO.fromEntity(profile));
            result.setRecommendations(profile.getRecommendations());
        }

        logger.info("Calculated total score: {} ({}%) for session: {}", totalScore, percentageScore,
                session.getSessionToken());
        return result;
    }

    @Override
    public List<DiagnosticDomainScoreDTO> calculateDomainScores(DiagnosticSession session) {
        logger.debug("Calculating domain scores for session: {}", session.getSessionToken());

        List<DiagnosticDomain> domains = domainRepository.findByDiagnosticIdOrderById(
                session.getDiagnostic().getId());
        List<DiagnosticDomainScoreDTO> domainScores = new ArrayList<>();

        for (DiagnosticDomain domain : domains) {
            // Get user answers for this domain
            List<DiagnosticUserAnswer> domainAnswers = userAnswerRepository
                    .findByDomainIdAndSessionId(domain.getId(), session.getId());

            if (domainAnswers.isEmpty()) {
                logger.warn("No answers found for domain {} in session {}", domain.getId(), session.getSessionToken());
                continue;
            }

            // Calculate raw score for domain
            Double rawScore = domainAnswers.stream()
                    .mapToDouble(answer -> answer.getNumericAnswer() != null ? answer.getNumericAnswer() : 0.0)
                    .sum();

            // Calculate weighted score
            Double weight = domain.getWeightFactor() != null ? domain.getWeightFactor() : 1.0;
            Double weightedScore = rawScore * weight;

            // Calculate percentage score for domain
            Double maxDomainScore = getMaxDomainScore(domain.getId());
            Double percentageScore = maxDomainScore > 0 ? (rawScore / maxDomainScore) * 100 : 0.0;

            // Create and save domain score entity
            DiagnosticDomainScore scoreEntity = new DiagnosticDomainScore(session, domain, rawScore);
            scoreEntity.setWeightedScore(weightedScore);
            scoreEntity.setPercentageScore(percentageScore);
            scoreEntity = domainScoreRepository.save(scoreEntity);

            // Create DTO
            DiagnosticDomainScoreDTO scoreDTO = DiagnosticDomainScoreDTO.fromEntity(scoreEntity);
            domainScores.add(scoreDTO);

            logger.debug("Domain {} score: raw={}, weighted={}, percentage={}%",
                    domain.getDomainName(), rawScore, weightedScore, percentageScore);
        }

        return domainScores;
    }

    @Override
    public Double calculateTotalScore(List<DiagnosticDomainScoreDTO> domainScores) {
        return domainScores.stream()
                .mapToDouble(score -> score.getWeightedScore() != null ? score.getWeightedScore() : 0.0)
                .sum();
    }

    @Override
    public Double calculatePercentageScore(Double totalScore, Double maxPossibleScore) {
        if (maxPossibleScore == null || maxPossibleScore <= 0) {
            return 0.0;
        }
        return (totalScore / maxPossibleScore) * 100;
    }

    @Override
    public DiagnosticResultDTO determineResultProfile(DiagnosticSession session, Double totalScore) {
        logger.debug("Determining result profile for session: {} with score: {}", session.getSessionToken(),
                totalScore);

        DiagnosticResultDTO result = new DiagnosticResultDTO();
        result.setTotalScore(totalScore);

        Optional<DiagnosticResultProfile> profileOpt = resultProfileRepository
                .findByDiagnosticIdAndScoreRange(session.getDiagnostic().getId(), totalScore);

        if (profileOpt.isPresent()) {
            DiagnosticResultProfile profile = profileOpt.get();
            result.setResultProfile(DiagnosticResultProfileDTO.fromEntity(profile));
            result.setRecommendations(profile.getRecommendations());
            logger.debug("Found matching profile: {} for score: {}", profile.getProfileName(), totalScore);
        } else {
            logger.warn("No matching profile found for score: {} in diagnostic: {}",
                    totalScore, session.getDiagnostic().getId());
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSessionReadyForCalculation(DiagnosticSession session) {
        logger.debug("Checking if session is ready for calculation: {}", session.getSessionToken());

        // Get all required questions for the diagnostic
        List<DiagnosticQuestion> requiredQuestions = questionRepository
                .findRequiredQuestionsByDiagnosticId(session.getDiagnostic().getId());

        // Get user answers for this session
        List<DiagnosticUserAnswer> userAnswers = userAnswerRepository.findBySessionId(session.getId());

        // Check if all required questions have been answered
        for (DiagnosticQuestion requiredQuestion : requiredQuestions) {
            boolean hasAnswer = userAnswers.stream()
                    .anyMatch(answer -> answer.getQuestion().getId() == requiredQuestion.getId() &&
                            (answer.getNumericAnswer() != null ||
                                    (answer.getTextAnswer() != null && !answer.getTextAnswer().trim().isEmpty())));

            if (!hasAnswer) {
                logger.debug("Missing answer for required question: {} in session: {}",
                        requiredQuestion.getId(), session.getSessionToken());
                return false;
            }
        }

        logger.debug("Session {} is ready for calculation", session.getSessionToken());
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getMaxPossibleScore(Integer diagnosticId) {
        logger.debug("Calculating max possible score for diagnostic: {}", diagnosticId);

        List<DiagnosticDomain> domains = domainRepository.findByDiagnosticIdOrderById(diagnosticId);
        Double maxScore = 0.0;

        for (DiagnosticDomain domain : domains) {
            Double maxDomainScore = getMaxDomainScore(domain.getId());
            Double weight = domain.getWeightFactor() != null ? domain.getWeightFactor() : 1.0;
            maxScore += maxDomainScore * weight;
        }

        logger.debug("Max possible score for diagnostic {}: {}", diagnosticId, maxScore);
        return maxScore;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageDomainScore(Integer domainId) {
        return domainScoreRepository.findAveragePercentageScoreByDomainId(domainId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosticDomainScoreDTO> getComparisonScores(Integer diagnosticId) {
        logger.debug("Getting comparison scores for diagnostic: {}", diagnosticId);

        List<DiagnosticDomain> domains = domainRepository.findByDiagnosticIdOrderById(diagnosticId);
        return domains.stream()
                .map(domain -> {
                    DiagnosticDomainScoreDTO dto = new DiagnosticDomainScoreDTO();
                    dto.setDomain(ca.coltip.data.dto.DiagnosticDomainDTO.fromEntity(domain));
                    dto.setPercentageScore(getAverageDomainScore(domain.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private Double getMaxDomainScore(Integer domainId) {
        logger.debug("Calculating max domain score for domain: {}", domainId);

        List<DiagnosticQuestion> questions = questionRepository.findByDomainIdOrderById(domainId);
        Double maxDomainScore = 0.0;

        for (DiagnosticQuestion question : questions) {
            Double maxQuestionScore = answerOptionRepository.findMaxValueByQuestionId(question.getId());
            if (maxQuestionScore != null) {
                maxDomainScore += maxQuestionScore;
            }
        }

        logger.debug("Max domain score for domain {}: {}", domainId, maxDomainScore);
        return maxDomainScore;
    }
}