package co.edu.unicauca.simulacro.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pregunta {
    private Long id;
    private String contexto;
    private String preguntaDirecta;
    private String justificacionRespuesta;
    private String bibliografia;
    private String competencia;
    private String tema;
    private String subtema;
    private EstadoPregunta estado;
    private NivelDificultad dificultad;
    private LocalDateTime fechaCreacion;
    private List<OpcionRespuesta> opciones = new ArrayList<>();

    public Pregunta() {
    }

    public Pregunta(Long id, String contexto, String preguntaDirecta, String competencia, String tema, NivelDificultad dificultad, EstadoPregunta estado, List<OpcionRespuesta> opciones) {
        this.id = id;
        this.contexto = contexto;
        this.preguntaDirecta = preguntaDirecta;
        this.competencia = competencia;
        this.tema = tema;
        this.dificultad = dificultad;
        this.estado = estado;
        this.fechaCreacion = LocalDateTime.now();
        if (opciones != null) {
            this.opciones = new ArrayList<>(opciones);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public String getPreguntaDirecta() {
        return preguntaDirecta;
    }

    public void setPreguntaDirecta(String preguntaDirecta) {
        this.preguntaDirecta = preguntaDirecta;
    }

    public String getJustificacionRespuesta() {
        return justificacionRespuesta;
    }

    public void setJustificacionRespuesta(String justificacionRespuesta) {
        this.justificacionRespuesta = justificacionRespuesta;
    }

    public String getBibliografia() {
        return bibliografia;
    }

    public void setBibliografia(String bibliografia) {
        this.bibliografia = bibliografia;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getSubtema() {
        return subtema;
    }

    public void setSubtema(String subtema) {
        this.subtema = subtema;
    }

    public EstadoPregunta getEstado() {
        return estado;
    }

    public void setEstado(EstadoPregunta estado) {
        this.estado = estado;
    }

    public NivelDificultad getDificultad() {
        return dificultad;
    }

    public void setDificultad(NivelDificultad dificultad) {
        this.dificultad = dificultad;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<OpcionRespuesta> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OpcionRespuesta> opciones) {
        this.opciones = opciones;
    }
}
