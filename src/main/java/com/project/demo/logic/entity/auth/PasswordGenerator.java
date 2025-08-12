package com.project.demo.logic.entity.auth;

import org.springframework.stereotype.Component;

/**
 * Generador de contraseñas seguras.
 * Permite crear contraseñas aleatorias con diferentes tipos de caracteres.
 */
@Component
public class PasswordGenerator {
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?";

    /**
     * Genera una contraseña aleatoria de la longitud especificada.
     *
     * @param length longitud de la contraseña
     * @return contraseña generada
     */
    public String generatePassword(int length) {
        StringBuilder password = new StringBuilder();
        String allCharacters = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;

        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * allCharacters.length());
            password.append(allCharacters.charAt(randomIndex));
        }

        return password.toString();
    }
}
