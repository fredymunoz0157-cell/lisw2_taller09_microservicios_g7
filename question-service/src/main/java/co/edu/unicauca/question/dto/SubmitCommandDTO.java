package co.edu.unicauca.question.dto;

/**
 * Alias compatible con el codigo de referencia de la Guia Paso a Paso (Seccion 4.2).
 */
public class SubmitCommandDTO extends SubmitQuestionCommand {
    public SubmitCommandDTO() {
        super();
    }

    public SubmitCommandDTO(Long questionId, String operationId) {
        super(questionId, operationId);
    }
}
