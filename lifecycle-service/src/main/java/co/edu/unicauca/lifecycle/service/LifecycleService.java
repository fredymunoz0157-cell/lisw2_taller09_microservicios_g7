package co.edu.unicauca.lifecycle.service;

import co.edu.unicauca.lifecycle.config.RabbitMQConfig;
import co.edu.unicauca.lifecycle.dto.QuestionSubmittedForReviewEvent;
import co.edu.unicauca.lifecycle.model.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LifecycleService {

    private final Map<Long, QuestionState> questionStates = new ConcurrentHashMap<>();
    private final Map<String, OperationResult> operationResults = new ConcurrentHashMap<>();
    private final Map<Long, List<HistorialModificacion>> stateHistory = new ConcurrentHashMap<>();
    private final AtomicLong stateIdSeq = new AtomicLong(1);
    private final AtomicLong historyIdSeq = new AtomicLong(1);

    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;
    private final String questionServiceUrl;

    public LifecycleService(
            RabbitTemplate rabbitTemplate,
            RestTemplate restTemplate,
            @Value("${services.question.url:http://localhost:8083}") String questionServiceUrl
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
        this.questionServiceUrl = questionServiceUrl;
    }

    public QuestionState registerOrResetState(Long questionId, QuestionStatus initialStatus, String changedBy, String comment) {
        QuestionState current = questionStates.get(questionId);
        QuestionStatus previous = current != null ? current.getCurrentState() : null;
        QuestionState state = new QuestionState(
                current != null ? current.getId() : stateIdSeq.getAndIncrement(),
                questionId,
                initialStatus,
                changedBy != null ? changedBy : "Docente",
                comment != null ? comment : "Registro inicial en estado " + initialStatus
        );
        questionStates.put(questionId, state);
        recordHistory(questionId, previous, initialStatus, state.getComment(), state.getChangedBy());
        return state;
    }

    /**
     * Procesa la transicion asincrona solicitada por RabbitMQ (Escenario 1.6: DRAFT -> PENDING_REVIEW).
     * Registra OperationResult (COMPLETED o FAILED) sin informar falsamente exito cuando falla.
     */
    public OperationResult processTransition(Long questionId, String targetStateStr, String operationId) {
        String opId = (operationId != null && !operationId.isBlank()) ? operationId : UUID.randomUUID().toString();
        try {
            QuestionState state = questionStates.get(questionId);
            if (state == null) {
                OperationResult failed = new OperationResult(
                        opId,
                        questionId,
                        "SUBMIT_FOR_REVIEW",
                        OperationStatus.FAILED,
                        null,
                        "Fallo la transicion asincrona: la pregunta con ID " + questionId + " no existe en Lifecycle Service."
                );
                operationResults.put(opId, failed);
                publishOperationResult(failed);
                return failed;
            }

            QuestionStatus targetStatus = QuestionStatus.fromString(targetStateStr);
            QuestionStatus previousStatus = state.getCurrentState();

            if (!state.canTransitionTo(targetStatus)) {
                OperationResult failed = new OperationResult(
                        opId,
                        questionId,
                        "SUBMIT_FOR_REVIEW",
                        OperationStatus.FAILED,
                        previousStatus.name(),
                        "Transicion asincrona rechazada: no se permite pasar de " + previousStatus + " a " + targetStatus
                );
                operationResults.put(opId, failed);
                publishOperationResult(failed);
                return failed;
            }

            state.applyTransition(
                    targetStatus,
                    "LifecycleCommandListener",
                    "Transicion asincrona procesada mediante RabbitMQ (OperationId: " + opId + ")"
            );
            recordHistory(questionId, previousStatus, targetStatus, state.getComment(), state.getChangedBy());
            notifyQuestionServiceStateChange(questionId, targetStatus.name(), "LifecycleService");

            OperationResult completed = new OperationResult(
                    opId,
                    questionId,
                    "SUBMIT_FOR_REVIEW",
                    OperationStatus.COMPLETED,
                    targetStatus.name(),
                    "Transicion completada exitosamente: " + previousStatus + " -> " + targetStatus
            );
            operationResults.put(opId, completed);
            publishOperationResult(completed);

            // Publicar evento QuestionSubmittedForReview (Figura 1)
            if (targetStatus == QuestionStatus.PENDING_REVIEW) {
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE_NAME,
                        RabbitMQConfig.ROUTING_KEY_QUESTION_SUBMITTED_EVENT,
                        new QuestionSubmittedForReviewEvent(opId, questionId, targetStatus.name())
                );
            }

            return completed;
        } catch (Exception ex) {
            OperationResult failed = new OperationResult(
                    opId,
                    questionId,
                    "SUBMIT_FOR_REVIEW",
                    OperationStatus.FAILED,
                    null,
                    "Error al procesar transicion asincrona: " + ex.getMessage()
            );
            operationResults.put(opId, failed);
            publishOperationResult(failed);
            return failed;
        }
    }

    /**
     * Procesa transiciones sincronas via REST (ej. PENDING_REVIEW -> IN_REVIEW, IN_REVIEW -> APPROVED/REJECTED,
     * o rechaza intentar aprobar una pregunta en DRAFT).
     */
    public QuestionState executeSynchronousTransition(Long questionId, String targetStateStr, String changedBy, String comment) {
        QuestionState state = questionStates.get(questionId);
        if (state == null) {
            throw new NoSuchElementException("No existe estado registrado para la pregunta ID: " + questionId);
        }
        QuestionStatus previousStatus = state.getCurrentState();
        QuestionStatus targetStatus = QuestionStatus.fromString(targetStateStr);

        state.applyTransition(
                targetStatus,
                changedBy != null ? changedBy : "LifecycleController",
                comment != null ? comment : "Transicion sincrona " + previousStatus + " -> " + targetStatus
        );
        recordHistory(questionId, previousStatus, targetStatus, state.getComment(), state.getChangedBy());
        notifyQuestionServiceStateChange(questionId, targetStatus.name(), state.getChangedBy());
        return state;
    }

    private void publishOperationResult(OperationResult result) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY_OPERATION_RESULT,
                    result
            );
        } catch (Exception ex) {
            System.out.println("[LifecycleService] Aviso al publicar OperationResult: " + ex.getMessage());
        }
    }

    private void notifyQuestionServiceStateChange(Long questionId, String newState, String changedBy) {
        try {
            restTemplate.postForEntity(
                    questionServiceUrl + "/questions/" + questionId + "/sync-state",
                    Map.of("state", newState, "changedBy", changedBy),
                    Void.class
            );
        } catch (Exception ignored) {
        }
    }

    private void recordHistory(Long questionId, QuestionStatus previous, QuestionStatus next, String detail, String responsible) {
        stateHistory.computeIfAbsent(questionId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(new HistorialModificacion(
                        historyIdSeq.getAndIncrement(),
                        questionId,
                        previous,
                        next,
                        detail,
                        responsible
                ));
    }

    public Optional<QuestionState> getQuestionState(Long questionId) {
        return Optional.ofNullable(questionStates.get(questionId));
    }

    public Optional<OperationResult> getOperationResult(String operationId) {
        return Optional.ofNullable(operationResults.get(operationId));
    }

    public List<HistorialModificacion> getQuestionHistory(Long questionId) {
        return stateHistory.getOrDefault(questionId, List.of());
    }
}
