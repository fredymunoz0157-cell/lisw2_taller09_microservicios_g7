package co.edu.unicauca.validation.controller;

import co.edu.unicauca.validation.dto.ValidationRequestDTO;
import co.edu.unicauca.validation.dto.ValidationResultDTO;
import co.edu.unicauca.validation.model.ValidationRule;
import co.edu.unicauca.validation.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/validate")
public class ValidationController {

    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * Endpoint sincrono POST /validate invocado por Question Service.
     */
    @PostMapping
    public ResponseEntity<ValidationResultDTO> validateQuestion(@RequestBody ValidationRequestDTO request) {
        ValidationResultDTO result = validationService.validate(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/rules")
    public ResponseEntity<List<ValidationRule>> listRules() {
        return ResponseEntity.ok(validationService.getRules());
    }
}
