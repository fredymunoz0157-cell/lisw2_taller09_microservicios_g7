package co.edu.unicauca.question.client;

import co.edu.unicauca.question.dto.QuestionDTO;
import co.edu.unicauca.question.dto.ValidationResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Cliente REST sincrono hacia Validation Service (:8082) y Lifecycle Service (:8084).
 */
@Component
public class ValidationClient {

    private final RestTemplate restTemplate;
    private final String validationServiceUrl;
    private final String lifecycleServiceUrl;

    public ValidationClient(
            RestTemplate restTemplate,
            @Value("${services.validation.url:http://localhost:8082}") String validationServiceUrl,
            @Value("${services.lifecycle.url:http://localhost:8084}") String lifecycleServiceUrl
    ) {
        this.restTemplate = restTemplate;
        this.validationServiceUrl = validationServiceUrl;
        this.lifecycleServiceUrl = lifecycleServiceUrl;
    }

    public ValidationResultDTO validateQuestion(QuestionDTO questionDTO) {
        try {
            ResponseEntity<ValidationResultDTO> response = restTemplate.postForEntity(
                    validationServiceUrl + "/validate",
                    questionDTO,
                    ValidationResultDTO.class
            );
            return response.getBody() != null
                    ? response.getBody()
                    : new ValidationResultDTO(false, List.of("Respuesta vacia del servicio de validacion."));
        } catch (Exception ex) {
            return new ValidationResultDTO(false, List.of("Error al conectar con Validation Service: " + ex.getMessage()));
        }
    }

    public void registerInitialStateInLifecycle(Long questionId, String initialState, String author) {
        try {
            Map<String, Object> body = Map.of(
                    "questionId", questionId,
                    "state", initialState,
                    "changedBy", author != null ? author : "Docente",
                    "comment", "Estado inicial registrado al crear/actualizar pregunta"
            );
            restTemplate.postForEntity(lifecycleServiceUrl + "/lifecycle/" + questionId + "/register", body, Map.class);
        } catch (Exception ex) {
            System.out.println("[QuestionService] Aviso: No se pudo pre-registrar estado en Lifecycle Service: " + ex.getMessage());
        }
    }
}
