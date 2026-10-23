package co.edu.unicauca.lifecycle.dto;

public class TransitionRequestDTO {
    private Long questionId;
    private String targetState;
    private String newState;
    private String changedBy;
    private String comment;

    public TransitionRequestDTO() {
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getTargetState() {
        return targetState != null ? targetState : newState;
    }

    public void setTargetState(String targetState) {
        this.targetState = targetState;
    }

    public String getNewState() {
        return newState != null ? newState : targetState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
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
