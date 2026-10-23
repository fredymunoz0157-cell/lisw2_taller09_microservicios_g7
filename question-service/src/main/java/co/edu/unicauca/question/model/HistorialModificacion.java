package co.edu.unicauca.question.model;

import java.time.LocalDateTime;

/**
 * Clase HistorialModificacion definida en el diagrama UML (Cluster: Banco de Preguntas).
 * Registra la trazabilidad de cambios de contenido y transiciones de estado de una Pregunta.
 */
public class HistorialModificacion {
    private Long id;
    private LocalDateTime fechaModificacion;
    private EstadoPregunta estadoAnterior;
    private EstadoPregunta estadoNuevo;
    private String detalleCambio;
    private String responsable;

    public HistorialModificacion() {
    }

    public HistorialModificacion(Long id, EstadoPregunta estadoAnterior, EstadoPregunta estadoNuevo, String detalleCambio, String responsable) {
        this.id = id;
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

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public EstadoPregunta getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(EstadoPregunta estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public EstadoPregunta getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(EstadoPregunta estadoNuevo) {
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
