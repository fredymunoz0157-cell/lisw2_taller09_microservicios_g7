package co.edu.unicauca.simulacro.model;

/**
 * Clase RespuestaEstudiante definida en el diagrama UML (Cluster: Simulacros y Seguimiento Academico).
 * Atributos:
 * - id: Long
 * - esCorrecta: boolean
 * - pregunta: Pregunta
 * - opcionSeleccionada: OpcionRespuesta
 * Metodo:
 * + verificarRespuesta(): boolean
 */
public class RespuestaEstudiante {
    private Long id;
    private boolean esCorrecta;
    private Pregunta pregunta;
    private OpcionRespuesta opcionSeleccionada;

    public RespuestaEstudiante() {
    }

    public RespuestaEstudiante(Long id, Pregunta pregunta, OpcionRespuesta opcionSeleccionada) {
        this.id = id;
        this.pregunta = pregunta;
        this.opcionSeleccionada = opcionSeleccionada;
        this.verificarRespuesta();
    }

    /**
     * Metodo del diagrama UML: +verificarRespuesta(): boolean
     */
    public boolean verificarRespuesta() {
        this.esCorrecta = (opcionSeleccionada != null && opcionSeleccionada.isEsCorrecta());
        return this.esCorrecta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public OpcionRespuesta getOpcionSeleccionada() {
        return opcionSeleccionada;
    }

    public void setOpcionSeleccionada(OpcionRespuesta opcionSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
    }
}
