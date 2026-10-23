package co.edu.unicauca.question.controller;

import co.edu.unicauca.question.dto.CreateQuestionResponseDTO;
import co.edu.unicauca.question.dto.OperationResultEventDTO;
import co.edu.unicauca.question.dto.QuestionDTO;
import co.edu.unicauca.question.model.Pregunta;
import co.edu.unicauca.question.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * Escenario 1.2 y 1.3: Creacion y validacion sincrona (POST /questions).
     * Si es valida persiste en DRAFT y retorna 201 Created.
     * Si falla retorna 400 Bad Request mediante @ExceptionHandler.
     */
    @PostMapping("/questions")
    public ResponseEntity<CreateQuestionResponseDTO> createQuestion(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody QuestionDTO request
    ) {
        Pregunta created = questionService.createQuestion(request);
        CreateQuestionResponseDTO response = new CreateQuestionResponseDTO(
                created.getId(),
                created.getStatus(),
                "Pregunta validada y registrada exitosamente en estado DRAFT."
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Permite corregir una pregunta en estado DRAFT o REJECTED (Caso de prueba: Corregir una pregunta rechazada).
     */
    @PutMapping("/questions/{questionId}")
    public ResponseEntity<CreateQuestionResponseDTO> updateQuestion(
            @PathVariable Long questionId,
            @RequestBody QuestionDTO request
    ) {
        Pregunta updated = questionService.updateQuestion(questionId, request);
        CreateQuestionResponseDTO response = new CreateQuestionResponseDTO(
                updated.getId(),
                updated.getStatus(),
                "Pregunta corregida exitosamente y lista para enviarse nuevamente a revision."
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Escenario 1.4 y 1.5: Solicitud asincrona de envio a revision (POST /questions/{questionId}/submit).
     * Publica comando SubmitQuestionForReview en RabbitMQ y responde inmediatamente 202 Accepted.
     */
    @PostMapping("/questions/{questionId}/submit")
    public ResponseEntity<Map<String, String>> submitForReview(@PathVariable Long questionId) {
        Map<String, String> response = questionService.submitForReview(questionId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PostMapping("/questions/{questionId}/sync-state")
    public ResponseEntity<Void> syncState(@PathVariable Long questionId, @RequestBody Map<String, String> payload) {
        questionService.syncStateFromLifecycle(
                questionId,
                payload.get("state"),
                payload.getOrDefault("changedBy", "LifecycleService")
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/questions")
    public ResponseEntity<Collection<Pregunta>> listQuestions() {
        return ResponseEntity.ok(questionService.findAll());
    }

    @GetMapping("/questions/{questionId}")
    public ResponseEntity<Pregunta> getQuestion(@PathVariable Long questionId) {
        return questionService.findById(questionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Consulta de estado de operacion asincrona (Figura 1 y Figura 2: GET /operations/{operationId}).
     */
    @GetMapping({"/operations/{operationId}", "/questions/operations/{operationId}"})
    public ResponseEntity<OperationResultEventDTO> getOperationStatus(@PathVariable String operationId) {
        return questionService.getOperationStatus(operationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "valid", false,
                "status", "BAD_REQUEST",
                "message", "Fallo en la validacion estructural de la pregunta.",
                "errors", ex.getMessage().split(" \\| ")
        ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", "INVALID_STATE",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "status", "NOT_FOUND",
                "message", ex.getMessage()
        ));
    }
}
