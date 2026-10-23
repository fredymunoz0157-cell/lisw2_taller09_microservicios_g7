package co.edu.unicauca.user.service;

import co.edu.unicauca.user.dto.LoginRequestDTO;
import co.edu.unicauca.user.dto.LoginResponseDTO;
import co.edu.unicauca.user.dto.UserDTO;
import co.edu.unicauca.user.model.RegistroAuditoria;
import co.edu.unicauca.user.model.Rol;
import co.edu.unicauca.user.model.Usuario;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private final Map<String, Usuario> usersByUsername = new ConcurrentHashMap<>();
    private final List<RegistroAuditoria> globalAuditLogs = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong auditSequence = new AtomicLong(1);

    @PostConstruct
    public void initDefaultUsers() {
        // 1. Docente / Autor
        Usuario docente = new Usuario(
                1L,
                "Docente Autor SaberPRO",
                "docente1",
                "SaberPro2026*",
                "docente1@unicauca.edu.co",
                true,
                List.of(Rol.DOCENTE, Rol.AUTOR)
        );
        // 2. Revisor de Pares
        Usuario revisor = new Usuario(
                2L,
                "Dra. Paola Bedoya (Revisora)",
                "revisor1",
                "Revisor2026*",
                "revisor1@unicauca.edu.co",
                true,
                List.of(Rol.REVISOR, Rol.DOCENTE)
        );
        // 3. Administrador
        Usuario admin = new Usuario(
                3L,
                "Ing. Pablo Mage (Administrador)",
                "admin",
                "Admin2026*",
                "admin@unicauca.edu.co",
                true,
                List.of(Rol.ADMINISTRADOR)
        );
        // 4. Estudiante
        Usuario estudiante = new Usuario(
                4L,
                "Estudiante Ingenieria de Sistemas",
                "estudiante1",
                "Est2026*",
                "estudiante1@unicauca.edu.co",
                true,
                List.of(Rol.ESTUDIANTE)
        );

        usersByUsername.put(docente.getUsername(), docente);
        usersByUsername.put(revisor.getUsername(), revisor);
        usersByUsername.put(admin.getUsername(), admin);
        usersByUsername.put(estudiante.getUsername(), estudiante);
    }

    public LoginResponseDTO authenticate(LoginRequestDTO request) {
        if (request == null || request.getUsername() == null) {
            registrarAuditoria(null, "LOGIN_FALLIDO", "Intento de login sin nombre de usuario");
            return new LoginResponseDTO(false, null, "Credenciales invalidas: usuario requerido.", null);
        }

        Usuario usuario = usersByUsername.get(request.getUsername());
        if (usuario == null || !usuario.autenticar(request.getPassword())) {
            registrarAuditoria(usuario, "LOGIN_FALLIDO", "Credenciales incorrectas para username: " + request.getUsername());
            return new LoginResponseDTO(false, null, "Acceso rechazado: usuario o contrasena incorrectos.", null);
        }

        registrarAuditoria(usuario, "LOGIN_EXITOSO", "Autenticacion exitosa para " + usuario.getUsername() + " con roles " + usuario.getRoles());

        UserDTO userDTO = new UserDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                usuario.isActivo(),
                usuario.getRoles()
        );
        String simulatedToken = "Bearer saberpro-token-" + usuario.getUsername() + "-" + usuario.getId();
        return new LoginResponseDTO(true, userDTO, "Autenticacion exitosa.", simulatedToken);
    }

    private void registrarAuditoria(Usuario usuario, String tipoEvento, String descripcion) {
        RegistroAuditoria registro = new RegistroAuditoria(
                auditSequence.getAndIncrement(),
                tipoEvento,
                descripcion
        );
        globalAuditLogs.add(registro);
        if (usuario != null) {
            usuario.agregarRegistroAuditoria(registro);
        }
    }

    public Optional<Usuario> findById(Long id) {
        return usersByUsername.values().stream()
                .filter(u -> Objects.equals(u.getId(), id))
                .findFirst();
    }

    public Collection<Usuario> findAllUsers() {
        return usersByUsername.values();
    }

    public List<RegistroAuditoria> getAuditLogs() {
        return new ArrayList<>(globalAuditLogs);
    }
}
