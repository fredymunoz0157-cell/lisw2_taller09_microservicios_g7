package co.edu.unicauca.question.dto;

/**
 * DTO CreateQuestionResponse <<DTO>> de la Figura 1.
 */
public class CreateQuestionResponseDTO {
    private Long id;
    private String status;
    private String message;

    public CreateQuestionResponseDTO() {
    }

    public CreateQuestionResponseDTO(Long id, String status, String message) {
        this.id = id;
        this.status = status;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
