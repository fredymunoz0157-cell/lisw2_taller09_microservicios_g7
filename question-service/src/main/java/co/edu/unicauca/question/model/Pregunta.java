package co.edu.unicauca.question.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Pregunta del diagrama UML e integracion con Question <<Entity>> de la Figura 1.
 * Metodos del UML:
 * + modificarContenido(contexto, pregunta, justificacion, bibliografia, competencia, tema, subtema): void
 * + cambiarEstado(nuevoEstado: EstadoPregunta, responsable: String): void
 * + esEditable(): boolean
 * Metodos de la Figura 1:
 * + create(): void
 * + updateContent(String newText): void
 */
public class Pregunta {
    private Long id;
    private String contexto;
    private String preguntaDirecta;
    private String justificacionRespuesta;
    private String bibliografia;
    private String competencia;
    private String tema;
    private String subtema;
    private EstadoPregunta estado = EstadoPregunta.BORRADOR;
    private NivelDificultad dificultad = NivelDificultad.MEDIO;
    private LocalDateTime fechaCreacion;
    private LocalDateTime updatedAt;
    private Long authorId;
    private String correctOptionId;
    private List<OpcionRespuesta> opciones = new ArrayList<>();
    private List<HistorialModificacion> historialModificaciones = new ArrayList<>();

    public Pregunta() {
        this.fechaCreacion = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Metodo de la Figura 1: +create()
     */
    public void create() {
        this.estado = EstadoPregunta.BORRADOR;
        this.fechaCreacion = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.historialModificaciones.add(new HistorialModificacion(
                (long) (this.historialModificaciones.size() + 1),
                null,
                EstadoPregunta.BORRADOR,
                "Creacion inicial de pregunta en estado DRAFT/BORRADOR",
                authorId != null ? "Autor-" + authorId : "Docente"
        ));
    }

    /**
     * Metodo del diagrama UML:
     * +modificarContenido(contexto: String, pregunta: String, justificacion: String,
     *                     bibliografia: String, competencia: String, tema: String, subtema: String): void
     */
    public void modificarContenido(
            String contexto,
            String pregunta,
            String justificacion,
            String bibliografia,
            String competencia,
            String tema,
            String subtema
    ) {
        if (!esEditable()) {
            throw new IllegalStateException("La pregunta en estado " + estado.getCode() + " no es editable. Solo preguntas en DRAFT o REJECTED pueden modificarse.");
        }
        this.contexto = contexto;
        this.preguntaDirecta = pregunta;
        this.justificacionRespuesta = justificacion;
        this.bibliografia = bibliografia;
        this.competencia = competencia;
        this.tema = tema;
        this.subtema = subtema;
        this.updatedAt = LocalDateTime.now();

        EstadoPregunta anterior = this.estado;
        if (this.estado == EstadoPregunta.RECHAZADA) {
            this.estado = EstadoPregunta.BORRADOR;
        }
        this.historialModificaciones.add(new HistorialModificacion(
                (long) (this.historialModificaciones.size() + 1),
                anterior,
                this.estado,
                "Modificacion y correccion de contenido de la pregunta",
                authorId != null ? "Autor-" + authorId : "Docente"
        ));
    }

    /**
     * Alias de la Figura 1: +updateContent(String text)
     */
    public void updateContent(String text) {
        modificarContenido(this.contexto, text, this.justificacionRespuesta, this.bibliografia, this.competencia, this.tema, this.subtema);
    }

    /**
     * Metodo del diagrama UML: +cambiarEstado(nuevoEstado: EstadoPregunta, responsable: String): void
     */
    public void cambiarEstado(EstadoPregunta nuevoEstado, String responsable) {
        EstadoPregunta anterior = this.estado;
        this.estado = nuevoEstado;
        this.updatedAt = LocalDateTime.now();
        this.historialModificaciones.add(new HistorialModificacion(
                (long) (this.historialModificaciones.size() + 1),
                anterior,
                nuevoEstado,
                "Cambio de estado sincronizado con Lifecycle Service: " + anterior.getCode() + " -> " + nuevoEstado.getCode(),
                responsable != null ? responsable : "LifecycleService"
        ));
    }

    /**
     * Metodo del diagrama UML: +esEditable(): boolean
     * Una pregunta solo puede editarse cuando esta en BORRADOR (DRAFT) o RECHAZADA (REJECTED).
     */
    public boolean esEditable() {
        return this.estado == EstadoPregunta.BORRADOR || this.estado == EstadoPregunta.RECHAZADA;
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

    public String getText() {
        return preguntaDirecta;
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

    public String getStatus() {
        return estado != null ? estado.getCode() : "DRAFT";
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getCorrectOptionId() {
        return correctOptionId;
    }

    public void setCorrectOptionId(String correctOptionId) {
        this.correctOptionId = correctOptionId;
    }

    public List<OpcionRespuesta> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OpcionRespuesta> opciones) {
        this.opciones = opciones;
    }

    public List<HistorialModificacion> getHistorialModificaciones() {
        return historialModificaciones;
    }

    public void setHistorialModificaciones(List<HistorialModificacion> historialModificaciones) {
        this.historialModificaciones = historialModificaciones;
    }
}
