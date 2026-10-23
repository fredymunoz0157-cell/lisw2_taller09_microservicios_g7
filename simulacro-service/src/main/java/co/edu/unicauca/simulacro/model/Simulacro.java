package co.edu.unicauca.simulacro.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase Simulacro definida en el diagrama UML (Cluster: Simulacros y Seguimiento Academico).
 * Atributos:
 * - id: Long
 * - titulo: String
 * - fechaGeneracion: LocalDateTime
 * - tiempoMaximoMinutos: int
 * Metodos:
 * + generarPreguntasAleatorias(competencia: String, tema: String, dificultad: NivelDificultad, cantidad: int): void
 * + estaEnTiempo(inicio: LocalDateTime, fin: LocalDateTime): boolean
 */
public class Simulacro {
    private Long id;
    private String titulo;
    private LocalDateTime fechaGeneracion;
    private int tiempoMaximoMinutos;
    private Usuario docenteConfigurador;
    private List<Pregunta> preguntas = new ArrayList<>();
    private List<Pregunta> bancoDisponible = new ArrayList<>();

    public Simulacro() {
        this.fechaGeneracion = LocalDateTime.now();
    }

    public Simulacro(Long id, String titulo, int tiempoMaximoMinutos, Usuario docenteConfigurador, List<Pregunta> bancoDisponible) {
        this.id = id;
        this.titulo = titulo;
        this.fechaGeneracion = LocalDateTime.now();
        this.tiempoMaximoMinutos = tiempoMaximoMinutos;
        this.docenteConfigurador = docenteConfigurador;
        if (bancoDisponible != null) {
            this.bancoDisponible = new ArrayList<>(bancoDisponible);
        }
    }

    /**
     * Metodo del diagrama UML:
     * +generarPreguntasAleatorias(competencia: String, tema: String, dificultad: NivelDificultad, cantidad: int): void
     */
    public void generarPreguntasAleatorias(String competencia, String tema, NivelDificultad dificultad, int cantidad) {
        List<Pregunta> candidatas = bancoDisponible.stream()
                .filter(p -> p.getEstado() == EstadoPregunta.PUBLICADA || p.getEstado() == EstadoPregunta.APROBADA)
                .filter(p -> competencia == null || competencia.isBlank() || competencia.equalsIgnoreCase(p.getCompetencia()))
                .filter(p -> tema == null || tema.isBlank() || tema.equalsIgnoreCase(p.getTema()))
                .filter(p -> dificultad == null || dificultad == p.getDificultad())
                .collect(Collectors.toList());

        Collections.shuffle(candidatas);
        int limit = Math.min(Math.max(cantidad, 1), candidatas.size());
        this.preguntas = new ArrayList<>(candidatas.subList(0, limit));
    }

    /**
     * Metodo del diagrama UML:
     * +estaEnTiempo(inicio: LocalDateTime, fin: LocalDateTime): boolean
     */
    public boolean estaEnTiempo(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null || fin.isBefore(inicio)) {
            return false;
        }
        long minutosTranscurridos = Duration.between(inicio, fin).toMinutes();
        return minutosTranscurridos <= this.tiempoMaximoMinutos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public int getTiempoMaximoMinutos() {
        return tiempoMaximoMinutos;
    }

    public void setTiempoMaximoMinutos(int tiempoMaximoMinutos) {
        this.tiempoMaximoMinutos = tiempoMaximoMinutos;
    }

    public Usuario getDocenteConfigurador() {
        return docenteConfigurador;
    }

    public void setDocenteConfigurador(Usuario docenteConfigurador) {
        this.docenteConfigurador = docenteConfigurador;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }
}
