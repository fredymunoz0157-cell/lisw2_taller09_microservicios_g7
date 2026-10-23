package co.edu.unicauca.review.dto;

import java.time.LocalDateTime;

/**
 * Evento ReviewerAssigned <<Event>> de la Figura 1 y Figura 2 (routing key: reviewer.assigned).
 */
public class ReviewerAssignedEventDTO {
    private Long questionId;
    private Long reviewerId;
    private Long assignedBy;
    private LocalDateTime occurredAt;

    public ReviewerAssignedEventDTO() {
    }

    public ReviewerAssignedEventDTO(Long questionId, Long reviewerId, Long assignedBy) {
        this.questionId = questionId;
        this.reviewerId = reviewerId;
        this.assignedBy = assignedBy;
        this.occurredAt = LocalDateTime.now();
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Long getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(Long assignedBy) {
        this.assignedBy = assignedBy;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}
