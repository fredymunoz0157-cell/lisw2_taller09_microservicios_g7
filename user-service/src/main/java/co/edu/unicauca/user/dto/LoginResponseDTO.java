package co.edu.unicauca.user.dto;

/**
 * DTO de respuesta de autenticacion (Figura 1: LoginResponse <<DTO>>).
 * Incluye: authenticated (boolean), user (UserDTO), message (String) y token simulado para encabezados Authorization.
 */
public class LoginResponseDTO {
    private boolean authenticated;
    private UserDTO user;
    private String message;
    private String token;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(boolean authenticated, UserDTO user, String message, String token) {
        this.authenticated = authenticated;
        this.user = user;
        this.message = message;
        this.token = token;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
