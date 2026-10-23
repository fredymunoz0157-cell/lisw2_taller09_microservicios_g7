package co.edu.unicauca.validation.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO ValidationResultDTO (Guia) / ValidationResult <<DTO>> (Figura 1).
 */
public class ValidationResultDTO {
    private boolean valid;
    private List<String> errors = new ArrayList<>();

    public ValidationResultDTO() {
    }

    public ValidationResultDTO(boolean valid, List<String> errors) {
        this.valid = valid;
        if (errors != null) {
            this.errors = new ArrayList<>(errors);
        }
    }

    /**
     * Metodo definido en la Figura 1: +hasErrors(): boolean
     */
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
