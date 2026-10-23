package co.edu.unicauca.question.model;

/**
 * Clase OpcionRespuesta (UML) / Option <<Entity>> (Figura 1).
 */
public class OpcionRespuesta {
    private String id;
    private String texto;
    private boolean esCorrecta;

    public OpcionRespuesta() {
    }

    public OpcionRespuesta(String id, String texto, boolean esCorrecta) {
        this.id = id;
        this.texto = texto;
        this.esCorrecta = esCorrecta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getText() {
        return texto;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }
}
