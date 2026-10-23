package co.edu.unicauca.lifecycle.model;

import java.time.LocalDateTime;

/**
 * Clase OperationResult <<Entity>> definida en la Figura 1 (Lifecycle Service).
 * Registra el resultado de las operaciones asincronas para verificacion posterior en Postman.
 */
public class OperationResult {
    private String id;
    private Long questionId;
    private String type;
    private OperationStatus status;
    private String newState;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public OperationResult() {
    }

    public OperationResult(String id, Long questionId, String type, OperationStatus status, String newState, String message) {
        this.id = id;
        this.questionId = questionId;
        this.type = type;
        this.status = status;
        this.newState = newState;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.completedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OperationStatus getStatus() {
        return status;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
