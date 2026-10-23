package co.edu.unicauca.lifecycle.dto;

import java.time.LocalDateTime;

/**
 * DTO de comando SubmitQuestionForReview consumido por @RabbitListener en Lifecycle Service.
 */
public class SubmitCommandDTO {
    private String operationId;
    private Long questionId;
    private Long requestedBy;
    private LocalDateTime requestedAt;

    public SubmitCommandDTO() {
    }

    public SubmitCommandDTO(Long questionId, String operationId) {
        this.questionId = questionId;
        this.operationId = operationId;
        this.requestedBy = 1L;
        this.requestedAt = LocalDateTime.now();
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
