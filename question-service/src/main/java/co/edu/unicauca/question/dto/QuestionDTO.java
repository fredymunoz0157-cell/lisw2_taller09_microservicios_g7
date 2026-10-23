package co.edu.unicauca.question.dto;

import java.util.List;

/**
 * DTO de creacion/actualizacion de preguntas (Guia: QuestionDTO / Figura 1: CreateQuestionRequest <<DTO>>).
 */
public class QuestionDTO {
    private String text;
    private String contexto;
    private String preguntaDirecta;
    private String justificacionRespuesta;
    private String bibliografia;
    private String competencia;
    private String tema;
    private String subtema;
    private String dificultad;
    private Long authorId;
    private List<OptionDTO> options;
    private String correctOptionId;

    public QuestionDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public List<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDTO> options) {
        this.options = options;
    }

    public String getCorrectOptionId() {
        return correctOptionId;
    }

    public void setCorrectOptionId(String correctOptionId) {
        this.correctOptionId = correctOptionId;
    }
}
