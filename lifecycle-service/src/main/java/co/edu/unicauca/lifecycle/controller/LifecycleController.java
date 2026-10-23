package co.edu.unicauca.lifecycle.controller;

import co.edu.unicauca.lifecycle.dto.TransitionRequestDTO;
import co.edu.unicauca.lifecycle.model.HistorialModificacion;
import co.edu.unicauca.lifecycle.model.OperationResult;
import co.edu.unicauca.lifecycle.model.QuestionState;
import co.edu.unicauca.lifecycle.model.QuestionStatus;
import co.edu.unicauca.lifecycle.service.LifecycleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/lifecycle")
public class LifecycleController {

    private final LifecycleService lifecycleService;

    public LifecycleController(LifecycleService lifecycleService) {
        this.lifecycleService = lifecycleService;
    }

    @PostMapping("/{questionId}/register")
    public ResponseEntity<QuestionState> registerInitialState(
            @PathVariable Long questionId,
            @RequestBody Map<String, String> body
    ) {
        QuestionStatus status = QuestionStatus.fromString(body.getOrDefault("state", "DRAFT"));
        QuestionState state = lifecycleService.registerOrResetState(
                questionId,
                status,
                body.get("changedBy"),
                body.get("comment")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(state);
    }

    /**
     * Consulta del estado actual de una pregunta (Guia: GET /lifecycle/{id}, Figura 2: GET /lifecycle/{questionId}).
     */
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionState> getQuestionState(@PathVariable Long questionId) {
        return lifecycleService.getQuestionState(questionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Solicitud de transicion de estado (Guia: POST /lifecycle/transition).
     */
    @PostMapping("/transition")
    public ResponseEntity<QuestionState> transition(@RequestBody TransitionRequestDTO request) {
        QuestionState updated = lifecycleService.executeSynchronousTransition(
                request.getQuestionId(),
                request.getTargetState(),
                request.getChangedBy(),
                request.getComment()
        );
        return ResponseEntity.ok(updated);
    }

    /**
     * Endpoint de transicion por path variable (Figura 2: POST /lifecycle/{questionId}/transition).
     */
    @PostMapping("/{questionId}/transition")
    public ResponseEntity<QuestionState> transitionById(
            @PathVariable Long questionId,
            @RequestBody TransitionRequestDTO request
    ) {
        QuestionState updated = lifecycleService.executeSynchronousTransition(
                questionId,
                request.getTargetState(),
                request.getChangedBy(),
                request.getComment()
        );
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/operations/{operationId}")
    public ResponseEntity<OperationResult> getOperationResult(@PathVariable String operationId) {
        return lifecycleService.getOperationResult(operationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{questionId}/history")
    public ResponseEntity<List<HistorialModificacion>> getHistory(@PathVariable Long questionId) {
        return ResponseEntity.ok(lifecycleService.getQuestionHistory(questionId));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTransition(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", "TRANSITION_REJECTED",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", "BAD_REQUEST",
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
