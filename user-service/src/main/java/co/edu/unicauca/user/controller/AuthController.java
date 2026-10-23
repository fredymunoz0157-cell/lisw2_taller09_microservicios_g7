package co.edu.unicauca.user.controller;

import co.edu.unicauca.user.dto.LoginRequestDTO;
import co.edu.unicauca.user.dto.LoginResponseDTO;
import co.edu.unicauca.user.model.RegistroAuditoria;
import co.edu.unicauca.user.model.Usuario;
import co.edu.unicauca.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Escenario 1.1: Autenticacion sincrona (POST /auth/login).
     * Si es exitosa retorna 200 OK con UserDTO y roles.
     * Si falla retorna 401 Unauthorized.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = userService.authenticate(request);
        if (!response.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<Collection<Usuario>> listUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Usuario> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/audit")
    public ResponseEntity<List<RegistroAuditoria>> getAuditLogs() {
        return ResponseEntity.ok(userService.getAuditLogs());
    }
}
