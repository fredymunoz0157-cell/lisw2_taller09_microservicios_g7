package co.edu.unicauca.question.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Evento ValidationFailed <<Event>> de la Figura 1.
 */
public class ValidationFailedEventDTO {
    private String operationId;
    private Long questionId;
    private List<String> errors;
    private LocalDateTime occurredAt;

    public ValidationFailedEventDTO() {
    }

    public ValidationFailedEventDTO(String operationId, Long questionId, List<String> errors) {
        this.operationId = operationId;
        this.questionId = questionId;
        this.errors = errors;
        this.occurredAt = LocalDateTime.now();
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}
