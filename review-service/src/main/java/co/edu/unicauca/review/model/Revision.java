package co.edu.unicauca.review.model;

import java.time.LocalDateTime;

/**
 * Clase Revision definida en el diagrama UML (Cluster: Revision por Pares).
 * Atributos:
 * - id: Long
 * - fechaAsignacion: LocalDateTime
 * - fechaEvaluacion: LocalDateTime
 * - observaciones: String
 * - aprobada: Boolean
 * - formatoEvaluacion: FormatoEvaluacion
 * Metodo:
 * + registrarEvaluacion(formato: FormatoEvaluacion, observaciones: String, aprobar: boolean): void
 */
public class Revision {
    private Long id;
    private Long preguntaId;
    private Long revisorId;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaEvaluacion;
    private String observaciones;
    private Boolean aprobada;
    private FormatoEvaluacion formatoEvaluacion;

    public Revision() {
        this.fechaAsignacion = LocalDateTime.now();
    }

    public Revision(Long id, Long preguntaId, Long revisorId) {
        this.id = id;
        this.preguntaId = preguntaId;
        this.revisorId = revisorId;
        this.fechaAsignacion = LocalDateTime.now();
    }

    /**
     * Metodo del diagrama UML:
     * +registrarEvaluacion(formato: FormatoEvaluacion, observaciones: String, aprobar: boolean): void
     */
    public void registrarEvaluacion(FormatoEvaluacion formato, String observaciones, boolean aprobar) {
        this.formatoEvaluacion = formato;
        this.observaciones = observaciones;
        this.fechaEvaluacion = LocalDateTime.now();
        if (aprobar && formato != null && !formato.esValidoParaAprobacion()) {
            throw new IllegalArgumentException("No se puede aprobar la pregunta porque el FormatoEvaluacion no cumple los criterios de calidad requeridos.");
        }
        this.aprobada = aprobar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(Long preguntaId) {
        this.preguntaId = preguntaId;
    }

    public Long getRevisorId() {
        return revisorId;
    }

    public void setRevisorId(Long revisorId) {
        this.revisorId = revisorId;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getAprobada() {
        return aprobada;
    }

    public void setAprobada(Boolean aprobada) {
        this.aprobada = aprobada;
    }

    public FormatoEvaluacion getFormatoEvaluacion() {
        return formatoEvaluacion;
    }

    public void setFormatoEvaluacion(FormatoEvaluacion formatoEvaluacion) {
        this.formatoEvaluacion = formatoEvaluacion;
    }
}
