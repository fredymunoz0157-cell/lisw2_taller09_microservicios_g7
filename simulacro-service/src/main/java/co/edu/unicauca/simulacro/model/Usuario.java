package co.edu.unicauca.simulacro.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Usuario del diagrama UML para asociacion con Simulacro, IntentoSimulacro y ReporteAcademico.
 */
public class Usuario {
    private Long id;
    private String nombreCompleto;
    private String username;
    private String passwordHash;
    private boolean activo;
    private List<Rol> roles = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(Long id, String nombreCompleto, String username, boolean activo, List<Rol> roles) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.username = username;
        this.activo = activo;
        if (roles != null) {
            this.roles = new ArrayList<>(roles);
        }
    }

    public boolean autenticar(String password) {
        return activo && passwordHash != null && passwordHash.equals(password);
    }

    public boolean tieneRol(Rol rol) {
        return roles != null && roles.contains(rol);
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
}
