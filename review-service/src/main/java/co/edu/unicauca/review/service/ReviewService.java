package co.edu.unicauca.review.service;

import co.edu.unicauca.review.dto.AssignReviewerRequestDTO;
import co.edu.unicauca.review.dto.EvaluateQuestionRequestDTO;
import co.edu.unicauca.review.dto.ReviewerAssignedEventDTO;
import co.edu.unicauca.review.messaging.EventPublisher;
import co.edu.unicauca.review.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReviewService {

    private final Map<Long, ReviewAssignment> assignmentsByQuestion = new ConcurrentHashMap<>();
    private final Map<Long, Revision> revisionsByQuestion = new ConcurrentHashMap<>();
    private final List<Review> reviews = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong assignmentIdSeq = new AtomicLong(1);
    private final AtomicLong reviewIdSeq = new AtomicLong(1);

    private final RestTemplate restTemplate;
    private final EventPublisher eventPublisher;
    private final String lifecycleServiceUrl;

    public ReviewService(
            RestTemplate restTemplate,
            EventPublisher eventPublisher,
            @Value("${services.lifecycle.url:http://localhost:8084}") String lifecycleServiceUrl
    ) {
        this.restTemplate = restTemplate;
        this.eventPublisher = eventPublisher;
        this.lifecycleServiceUrl = lifecycleServiceUrl;
    }

    /**
     * Escenario 1.7 y Figura 2 (Fase 2 - Asignacion del revisor):
     * 1. Consulta GET /lifecycle/{questionId} para verificar que el estado sea PENDING_REVIEW.
     * 2. Solicita POST /lifecycle/{questionId}/transition hacia IN_REVIEW.
     * 3. Registra ReviewAssignment y Revision.
     * 4. Publica evento asincrono ReviewerAssigned en RabbitMQ hacia Notification Service.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> assignReviewer(AssignReviewerRequestDTO request) {
        if (request.getQuestionId() == null || request.getReviewerId() == null) {
            throw new IllegalArgumentException("questionId y reviewerId son obligatorios para asignar un revisor.");
        }

        // 1. Verificar elegibilidad de la pregunta en Lifecycle Service (GET /lifecycle/{questionId})
        Map<String, Object> stateData;
        try {
            ResponseEntity<Map> stateResponse = restTemplate.getForEntity(
                    lifecycleServiceUrl + "/lifecycle/" + request.getQuestionId(),
                    Map.class
            );
            stateData = stateResponse.getBody();
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo verificar el estado de la pregunta ID " + request.getQuestionId() + " en Lifecycle Service.");
        }

        String currentStatus = stateData != null ? String.valueOf(stateData.get("state")) : "UNKNOWN";
        if (!"PENDING_REVIEW".equalsIgnoreCase(currentStatus)) {
            throw new IllegalStateException(
                    "La pregunta ID " + request.getQuestionId() + " no es elegible para asignacion de revisor porque su estado actual es "
                            + currentStatus + " (se requiere PENDING_REVIEW)."
            );
        }

        // 2. Solicitar a Lifecycle Service la transicion PENDING_REVIEW -> IN_REVIEW
        Map<String, Object> transitionPayload = Map.of(
                "questionId", request.getQuestionId(),
                "targetState", "IN_REVIEW",
                "changedBy", "ReviewService (Revisor ID: " + request.getReviewerId() + ")",
                "comment", "Asignacion de revisor de pares confirmada"
        );
        restTemplate.postForEntity(
                lifecycleServiceUrl + "/lifecycle/" + request.getQuestionId() + "/transition",
                transitionPayload,
                Map.class
        );

        // 3. Registrar la asignacion (ReviewAssignment + Revision del UML)
        ReviewAssignment assignment = new ReviewAssignment(
                assignmentIdSeq.getAndIncrement(),
                request.getQuestionId(),
                request.getReviewerId()
        );
        assignmentsByQuestion.put(request.getQuestionId(), assignment);

        Revision revision = new Revision(
                assignment.getId(),
                request.getQuestionId(),
                request.getReviewerId()
        );
        revisionsByQuestion.put(request.getQuestionId(), revision);

        // 4. Publicar evento ReviewerAssigned en RabbitMQ (Escenario 1.8)
        ReviewerAssignedEventDTO event = new ReviewerAssignedEventDTO(
                request.getQuestionId(),
                request.getReviewerId(),
                request.getAssignedBy() != null ? request.getAssignedBy() : 3L
        );
        eventPublisher.publishReviewerAssigned(event);

        return Map.of(
                "message", "Revisor asignado exitosamente. Transicion a IN_REVIEW aplicada y evento ReviewerAssigned publicado.",
                "assignment", assignment,
                "newState", "IN_REVIEW"
        );
    }

    /**
     * Evalua una pregunta en estado IN_REVIEW para aprobarla (APPROVED) o rechazarla (REJECTED) con motivo registrado.
     * Integra las clases Review (Figura 1) y Revision + FormatoEvaluacion (UML).
     */
    public Map<String, Object> evaluateQuestion(EvaluateQuestionRequestDTO request) {
        if (request.getQuestionId() == null) {
            throw new IllegalArgumentException("El identificador de la pregunta (questionId) es obligatorio.");
        }

        boolean isApprove = Boolean.TRUE.equals(request.getAprobar())
                || "APPROVED".equalsIgnoreCase(request.getResult())
                || "APROBADA".equalsIgnoreCase(request.getResult());

        String targetState = isApprove ? "APPROVED" : "REJECTED";
        String comment = request.getComments();

        Review review = new Review(
                reviewIdSeq.getAndIncrement(),
                request.getQuestionId(),
                request.getReviewerId() != null ? request.getReviewerId() : 2L,
                comment
        );

        if (isApprove) {
            review.approve(comment != null ? comment : "Pregunta cumple criterios de calidad pedagogica, tecnica y estructural.");
        } else {
            review.reject(comment);
        }

        FormatoEvaluacion formato = request.getFormatoEvaluacion();
        if (formato == null) {
            formato = new FormatoEvaluacion(
                    review.getId(),
                    isApprove,
                    isApprove,
                    isApprove,
                    isApprove ? 90 : 45
            );
        }

        Revision revision = revisionsByQuestion.computeIfAbsent(
                request.getQuestionId(),
                qId -> new Revision(review.getId(), qId, review.getReviewerId())
        );
        revision.registrarEvaluacion(formato, review.getComments(), isApprove);

        // Solicitar a Lifecycle Service la transicion IN_REVIEW -> APPROVED o REJECTED
        Map<String, Object> transitionPayload = Map.of(
                "questionId", request.getQuestionId(),
                "targetState", targetState,
                "changedBy", "Revisor-" + review.getReviewerId(),
                "comment", review.getComments()
        );
        try {
            restTemplate.postForEntity(
                    lifecycleServiceUrl + "/lifecycle/" + request.getQuestionId() + "/transition",
                    transitionPayload,
                    Map.class
            );
        } catch (Exception ex) {
            throw new IllegalStateException("Transicion rechazada por Lifecycle Service: " + ex.getMessage());
        }

        ReviewAssignment assignment = assignmentsByQuestion.get(request.getQuestionId());
        if (assignment != null) {
            assignment.updateStatus(AssignmentStatus.COMPLETED);
        }
        reviews.add(review);

        return Map.of(
                "message", "Evaluacion de pares registrada exitosamente. Nuevo estado: " + targetState,
                "review", review,
                "revisionUML", revision,
                "newState", targetState
        );
    }

    public Optional<Revision> getRevisionByQuestion(Long questionId) {
        return Optional.ofNullable(revisionsByQuestion.get(questionId));
    }

    public List<Review> getAllReviews() {
        return new ArrayList<>(reviews);
    }
}
