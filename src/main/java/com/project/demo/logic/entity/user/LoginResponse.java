package com.project.demo.logic.entity.user;

/**
 * Representa la respuesta al iniciar sesión.
 * Incluye el token de autenticación, el usuario autenticado y el tiempo de expiración.
 */
public class LoginResponse {
    private String token;

    private User authUser;

    private long expiresIn;

    /**
     * Obtiene el token de autenticación.
     *
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * Establece el token de autenticación.
     *
     * @param token token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Obtiene el tiempo de expiración del token.
     *
     * @return tiempo de expiración en segundos
     */
    public long getExpiresIn() {
        return expiresIn;
    }

    /**
     * Establece el tiempo de expiración del token.
     *
     * @param expiresIn tiempo de expiración en segundos
     */
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * Obtiene el usuario autenticado.
     *
     * @return usuario autenticado
     */
    public User getAuthUser() {
        return authUser;
    }

    /**
     * Establece el usuario autenticado.
     *
     * @param authUser usuario autenticado
     */
    public void setAuthUser(User authUser) {
        this.authUser = authUser;
    }
}
