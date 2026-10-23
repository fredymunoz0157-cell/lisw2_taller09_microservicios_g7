package co.edu.unicauca.review.dto;

import co.edu.unicauca.review.model.FormatoEvaluacion;

public class EvaluateQuestionRequestDTO {
    private Long questionId;
    private Long reviewerId;
    private String result; // APPROVED o REJECTED
    private Boolean aprobar;
    private String comments;
    private String observaciones;
    private FormatoEvaluacion formatoEvaluacion;

    public EvaluateQuestionRequestDTO() {
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Boolean getAprobar() {
        return aprobar;
    }

    public void setAprobar(Boolean aprobar) {
        this.aprobar = aprobar;
    }

    public String getComments() {
        return comments != null ? comments : observaciones;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getObservaciones() {
        return observaciones != null ? observaciones : comments;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public FormatoEvaluacion getFormatoEvaluacion() {
        return formatoEvaluacion;
    }

    public void setFormatoEvaluacion(FormatoEvaluacion formatoEvaluacion) {
        this.formatoEvaluacion = formatoEvaluacion;
    }
}
