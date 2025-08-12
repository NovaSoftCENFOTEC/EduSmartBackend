package com.project.demo.logic.entity.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

/**
 * Servicio para la autenticación de usuarios mediante Google.
 * Verifica el token de Google y retorna el usuario autenticado si existe en la base de datos.
 */
@Service
public class GoogleAuthService {

    @Autowired
    private UserRepository userRepository;

    @Value("${google.client-id}")
    private String googleClientId;

    /**
     * Autentica a un usuario usando el token de Google.
     *
     * @param idTokenString token de Google proporcionado por el cliente
     * @return usuario autenticado si existe en la base de datos
     * @throws GeneralSecurityException si ocurre un error de seguridad
     * @throws IOException              si ocurre un error de entrada/salida
     */
    public User authenticateGoogleUser(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            Optional<User> existingUser = userRepository.findByEmail(email);

            if (existingUser.isPresent()) {
                return existingUser.get();
            } else {
                throw new BadCredentialsException("Usuario no encontrado. Por favor, contrata a un administrador.");
            }
        } else {
            throw new IllegalArgumentException("Google ID token invalido. Por favor, verifica el token.");
        }
    }


}
