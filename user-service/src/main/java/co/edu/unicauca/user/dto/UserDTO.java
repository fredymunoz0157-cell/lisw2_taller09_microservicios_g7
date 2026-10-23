package co.edu.unicauca.user.dto;

import co.edu.unicauca.user.model.Rol;
import java.util.List;

/**
 * DTO con los datos basicos del usuario autenticado y sus roles (Guia: UserDTO).
 */
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private boolean activo;
    private List<Rol> roles;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String fullName, String email, boolean activo, List<Rol> roles) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.activo = activo;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
}
