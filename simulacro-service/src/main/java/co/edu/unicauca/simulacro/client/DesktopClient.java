package co.edu.unicauca.simulacro.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Clase DesktopClient representada en la Figura 1 (Diagrama de Contextos).
 * Metodos:
 * + login(username, password)
 * + createQuestion(token, questionPayload)
 * + submitQuestionForReview(questionId)
 * + checkOperationStatus(operationId)
 */
@Component
public class DesktopClient {

    private final RestTemplate restTemplate;
    private final String userServiceUrl;
    private final String questionServiceUrl;

    public DesktopClient(
            RestTemplate restTemplate,
            @Value("${services.user.url:http://localhost:8081}") String userServiceUrl,
            @Value("${services.question.url:http://localhost:8083}") String questionServiceUrl
    ) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
        this.questionServiceUrl = questionServiceUrl;
    }

    public Map<?, ?> login(String username, String password) {
        ResponseEntity<Map> response = restTemplate.postForEntity(
                userServiceUrl + "/auth/login",
                Map.of("username", username, "password", password),
                Map.class
        );
        return response.getBody();
    }

    public Map<?, ?> createQuestion(String token, Map<String, Object> questionData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) {
            headers.set("Authorization", token);
        }
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(questionData, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                questionServiceUrl + "/questions",
                entity,
                Map.class
        );
        return response.getBody();
    }

    public Map<?, ?> submitQuestionForReview(Long questionId) {
        ResponseEntity<Map> response = restTemplate.postForEntity(
                questionServiceUrl + "/questions/" + questionId + "/submit",
                null,
                Map.class
        );
        return response.getBody();
    }

    public Map<?, ?> checkOperationStatus(String operationId) {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                questionServiceUrl + "/operations/" + operationId,
                Map.class
        );
        return response.getBody();
    }
}
