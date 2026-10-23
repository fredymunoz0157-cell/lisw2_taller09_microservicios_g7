package co.edu.unicauca.review.model;

import java.time.LocalDateTime;

/**
 * Clase ReviewAssignment <<Entity>> de la Figura 1.
 */
public class ReviewAssignment {
    private Long id;
    private Long questionId;
    private Long reviewerId;
    private LocalDateTime assignedAt;
    private AssignmentStatus status;

    public ReviewAssignment() {
    }

    public ReviewAssignment(Long id, Long questionId, Long reviewerId) {
        this.id = id;
        this.questionId = questionId;
        this.reviewerId = reviewerId;
        this.assignReviewer();
    }

    public void assignReviewer() {
        this.assignedAt = LocalDateTime.now();
        this.status = AssignmentStatus.ASSIGNED;
    }

    public void updateStatus(AssignmentStatus newStatus) {
        this.status = newStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }
}
