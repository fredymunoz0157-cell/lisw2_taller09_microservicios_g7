package co.edu.unicauca.lifecycle.model;

import java.time.LocalDateTime;

/**
 * Clase HistorialModificacion del diagrama UML para auditar cada cambio de estado en Lifecycle Service.
 */
public class HistorialModificacion {
    private Long id;
    private Long questionId;
    private LocalDateTime fechaModificacion;
    private QuestionStatus estadoAnterior;
    private QuestionStatus estadoNuevo;
    private String detalleCambio;
    private String responsable;

    public HistorialModificacion() {
    }

    public HistorialModificacion(Long id, Long questionId, QuestionStatus estadoAnterior, QuestionStatus estadoNuevo, String detalleCambio, String responsable) {
        this.id = id;
        this.questionId = questionId;
        this.fechaModificacion = LocalDateTime.now();
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.detalleCambio = detalleCambio;
        this.responsable = responsable;
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

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public QuestionStatus getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(QuestionStatus estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public QuestionStatus getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(QuestionStatus estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public String getDetalleCambio() {
        return detalleCambio;
    }

    public void setDetalleCambio(String detalleCambio) {
        this.detalleCambio = detalleCambio;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
}
