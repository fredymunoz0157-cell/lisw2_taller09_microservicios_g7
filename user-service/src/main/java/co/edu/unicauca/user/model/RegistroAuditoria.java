package co.edu.unicauca.user.model;

import java.time.LocalDateTime;

/**
 * Clase RegistroAuditoria definida en el diagrama UML (Cluster: Gestion de Usuarios y Auditoria).
 * Registra los eventos de seguridad y acciones asociadas a un Usuario.
 */
public class RegistroAuditoria {
    private Long id;
    private LocalDateTime fechaHora;
    private String tipoEvento;
    private String descripcion;

    public RegistroAuditoria() {
    }

    public RegistroAuditoria(Long id, String tipoEvento, String descripcion) {
        this.id = id;
        this.fechaHora = LocalDateTime.now();
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
