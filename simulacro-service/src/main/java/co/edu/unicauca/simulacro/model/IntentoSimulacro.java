package co.edu.unicauca.simulacro.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase IntentoSimulacro definida en el diagrama UML (Cluster: Simulacros y Seguimiento Academico).
 * Atributos:
 * - id: Long
 * - fechaInicio: LocalDateTime
 * - fechaFin: LocalDateTime
 * - calificacion: double
 * Metodos:
 * + calificarAutomaticamente(): double
 * + finalizarIntento(): void
 */
public class IntentoSimulacro {
    private Long id;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private double calificacion;
    private Usuario estudiante;
    private Simulacro simulacro;
    private List<RespuestaEstudiante> respuestas = new ArrayList<>();

    public IntentoSimulacro() {
        this.fechaInicio = LocalDateTime.now();
    }

    public IntentoSimulacro(Long id, Usuario estudiante, Simulacro simulacro, List<RespuestaEstudiante> respuestas) {
        this.id = id;
        this.fechaInicio = LocalDateTime.now();
        this.estudiante = estudiante;
        this.simulacro = simulacro;
        if (respuestas != null) {
            this.respuestas = new ArrayList<>(respuestas);
        }
    }

    /**
     * Metodo del diagrama UML: +calificarAutomaticamente(): double
     * Calcula la calificacion en escala Saber PRO (0.0 a 300.0 puntos).
     */
    public double calificarAutomaticamente() {
        if (respuestas == null || respuestas.isEmpty()) {
            this.calificacion = 0.0;
            return this.calificacion;
        }
        long correctas = respuestas.stream()
                .filter(RespuestaEstudiante::verificarRespuesta)
                .count();
        this.calificacion = Math.round(((double) correctas / respuestas.size()) * 300.0 * 100.0) / 100.0;
        return this.calificacion;
    }

    /**
     * Metodo del diagrama UML: +finalizarIntento(): void
     */
    public void finalizarIntento() {
        this.fechaFin = LocalDateTime.now();
        calificarAutomaticamente();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public Usuario getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Usuario estudiante) {
        this.estudiante = estudiante;
    }

    public Simulacro getSimulacro() {
        return simulacro;
    }

    public void setSimulacro(Simulacro simulacro) {
        this.simulacro = simulacro;
    }

    public List<RespuestaEstudiante> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<RespuestaEstudiante> respuestas) {
        this.respuestas = respuestas;
    }
}
