package co.edu.unicauca.lifecycle.dto;

import java.time.LocalDateTime;

/**
 * Evento QuestionSubmittedForReview <<Event>> de la Figura 1.
 */
public class QuestionSubmittedForReviewEvent {
    private String operationId;
    private Long questionId;
    private String newState;
    private LocalDateTime occurredAt;

    public QuestionSubmittedForReviewEvent() {
    }

    public QuestionSubmittedForReviewEvent(String operationId, Long questionId, String newState) {
        this.operationId = operationId;
        this.questionId = questionId;
        this.newState = newState;
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

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}
