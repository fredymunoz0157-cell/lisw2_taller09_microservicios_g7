package co.edu.unicauca.lifecycle.model;

import java.time.LocalDateTime;

/**
 * Clase QuestionState <<Entity>> definida en la Figura 1 (Lifecycle Service).
 * Autoridad unica de validacion y aplicacion de transiciones de estado:
 * + canTransitionTo(newState: QuestionStatus): boolean
 * + applyTransition(newState: QuestionStatus): void
 * + getCurrentState(): QuestionStatus
 */
public class QuestionState {
    private Long id;
    private Long questionId;
    private QuestionStatus state;
    private LocalDateTime changedAt;
    private String changedBy;
    private String comment;

    public QuestionState() {
    }

    public QuestionState(Long id, Long questionId, QuestionStatus state, String changedBy, String comment) {
        this.id = id;
        this.questionId = questionId;
        this.state = state;
        this.changedAt = LocalDateTime.now();
        this.changedBy = changedBy;
        this.comment = comment;
    }

    /**
     * Reglas de transicion del Ciclo de Vida de Preguntas Saber PRO:
     * - DRAFT -> PENDING_REVIEW | ARCHIVED
     * - PENDING_REVIEW -> IN_REVIEW | REJECTED
     * - IN_REVIEW -> APPROVED | REJECTED
     * - REJECTED -> DRAFT | PENDING_REVIEW | ARCHIVED
     * - APPROVED -> PUBLISHED | ARCHIVED
     * - PUBLISHED -> ARCHIVED
     */
    public boolean canTransitionTo(QuestionStatus newState) {
        if (this.state == null || newState == null || this.state == newState) {
            return false;
        }
        return switch (this.state) {
            case DRAFT -> newState == QuestionStatus.PENDING_REVIEW || newState == QuestionStatus.ARCHIVED;
            case PENDING_REVIEW -> newState == QuestionStatus.IN_REVIEW || newState == QuestionStatus.REJECTED;
            case IN_REVIEW -> newState == QuestionStatus.APPROVED || newState == QuestionStatus.REJECTED;
            case REJECTED -> newState == QuestionStatus.DRAFT
                    || newState == QuestionStatus.PENDING_REVIEW
                    || newState == QuestionStatus.ARCHIVED;
            case APPROVED -> newState == QuestionStatus.PUBLISHED || newState == QuestionStatus.ARCHIVED;
            case PUBLISHED -> newState == QuestionStatus.ARCHIVED;
            case ARCHIVED -> false;
        };
    }

    public void applyTransition(QuestionStatus newState) {
        applyTransition(newState, "System", "Transicion aplicada hacia " + newState);
    }

    public void applyTransition(QuestionStatus newState, String changedBy, String comment) {
        if (!canTransitionTo(newState)) {
            throw new IllegalStateException(
                    "Transicion de estado no permitida: " + this.state + " -> " + newState
            );
        }
        this.state = newState;
        this.changedAt = LocalDateTime.now();
        if (changedBy != null) {
            this.changedBy = changedBy;
        }
        if (comment != null) {
            this.comment = comment;
        }
    }

    public QuestionStatus getCurrentState() {
        return this.state;
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

    public QuestionStatus getState() {
        return state;
    }

    public void setState(QuestionStatus state) {
        this.state = state;
    }

    public String getStatus() {
        return state != null ? state.name() : null;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
