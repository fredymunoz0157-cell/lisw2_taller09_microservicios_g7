package co.edu.unicauca.question.dto;

import java.time.LocalDateTime;

/**
 * Comando asincrono SubmitQuestionForReview <<Command>> (Guia: SubmitQuestionCommand / SubmitCommandDTO / Figura 1).
 */
public class SubmitQuestionCommand {
    private String operationId;
    private Long questionId;
    private Long requestedBy;
    private LocalDateTime requestedAt;

    public SubmitQuestionCommand() {
    }

    public SubmitQuestionCommand(Long questionId, String operationId) {
        this.questionId = questionId;
        this.operationId = operationId;
        this.requestedBy = 1L;
        this.requestedAt = LocalDateTime.now();
    }

    public SubmitQuestionCommand(String operationId, Long questionId, Long requestedBy, LocalDateTime requestedAt) {
        this.operationId = operationId;
        this.questionId = questionId;
        this.requestedBy = requestedBy;
        this.requestedAt = requestedAt;
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

    public Long getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Long requestedBy) {
        this.requestedBy = requestedBy;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }
}
