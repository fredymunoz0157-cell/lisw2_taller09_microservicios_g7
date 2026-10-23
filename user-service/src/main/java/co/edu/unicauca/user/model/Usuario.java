package co.edu.unicauca.user.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de dominio Usuario (UML: Usuario / Figura 1: User <<Entity>>).
 * Integra los atributos y metodos del diagrama UML y del diagrama de contextos:
 * - autenticar(password: String): boolean  / validatePassword(password: String): boolean
 * - tieneRol(rol: Rol): boolean            / hasRole(role: String): boolean
 * - Relacion 1..* con Rol y 0..* con RegistroAuditoria.
 */
public class Usuario {
    private Long id;
    private String nombreCompleto;
    private String username;
    private String passwordHash;
    private String email;
    private boolean activo;
    private List<Rol> roles = new ArrayList<>();
    private List<RegistroAuditoria> registrosAuditoria = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(Long id, String nombreCompleto, String username, String passwordHash, String email, boolean activo, List<Rol> roles) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.activo = activo;
        if (roles != null) {
            this.roles = new ArrayList<>(roles);
        }
    }

    /**
     * Metodo del diagrama UML: +autenticar(password: String): boolean
     */
    public boolean autenticar(String password) {
        if (!this.activo || password == null || this.passwordHash == null) {
            return false;
        }
        return this.passwordHash.equals(password);
    }

    /**
     * Alias de la Figura 1 (User Entity): +validatePassword(password: String): boolean
     */
    public boolean validatePassword(String password) {
        return autenticar(password);
    }

    /**
     * Metodo del diagrama UML: +tieneRol(rol: Rol): boolean
     */
    public boolean tieneRol(Rol rol) {
        return this.roles != null && this.roles.contains(rol);
    }

    /**
     * Alias de la Figura 1 (User Entity): +hasRole(role: String): boolean
     */
    public boolean hasRole(String role) {
        if (role == null || this.roles == null) {
            return false;
        }
        return this.roles.stream().anyMatch(r -> r.name().equalsIgnoreCase(role));
    }

    public void agregarRegistroAuditoria(RegistroAuditoria registro) {
        this.registrosAuditoria.add(registro);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getFullName() {
        return nombreCompleto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public List<RegistroAuditoria> getRegistrosAuditoria() {
        return registrosAuditoria;
    }

    public void setRegistrosAuditoria(List<RegistroAuditoria> registrosAuditoria) {
        this.registrosAuditoria = registrosAuditoria;
    }
}
