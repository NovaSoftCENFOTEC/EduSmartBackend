package com.project.demo.rest.auth.dto;

/**
 * DTO para la solicitud de inicio de sesión con Google.
 * Contiene el token de autenticación de Google.
 */
public class GoogleLoginRequestDto {
    private String idToken;

    /**
     * Constructor por defecto.
     */
    public GoogleLoginRequestDto() {}

    /**
     * Constructor con el token de Google.
     * @param idToken token de Google
     */
    public GoogleLoginRequestDto(String idToken) {
        this.idToken = idToken;
    }

    /**
     * Obtiene el token de Google.
     * @return idToken
     */
    public String getIdToken() {
        return idToken;
    }

    /**
     * Establece el token de Google.
     * @param idToken token de Google
     */
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
