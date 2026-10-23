package co.edu.unicauca.review.model;

import java.time.LocalDateTime;

/**
 * Clase Review <<Entity>> de la Figura 1.
 */
public class Review {
    private Long id;
    private Long questionId;
    private Long reviewerId;
    private ReviewResult result;
    private String comments;
    private LocalDateTime createdAt;

    public Review() {
        this.createdAt = LocalDateTime.now();
    }

    public Review(Long id, Long questionId, Long reviewerId, String comments) {
        this.id = id;
        this.questionId = questionId;
        this.reviewerId = reviewerId;
        this.comments = comments;
        this.createdAt = LocalDateTime.now();
    }

    public void approve(String comments) {
        this.result = ReviewResult.APPROVED;
        this.comments = comments;
        this.createdAt = LocalDateTime.now();
    }

    public void reject(String comments) {
        if (comments == null || comments.isBlank()) {
            throw new IllegalArgumentException("Para rechazar una pregunta en IN_REVIEW es obligatorio registrar el motivo de rechazo.");
        }
        this.result = ReviewResult.REJECTED;
        this.comments = comments;
        this.createdAt = LocalDateTime.now();
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

    public ReviewResult getResult() {
        return result;
    }

    public void setResult(ReviewResult result) {
        this.result = result;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
