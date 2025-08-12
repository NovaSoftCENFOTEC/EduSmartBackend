package com.project.demo.rest.user;

import com.project.demo.logic.entity.auth.PasswordGenerator;
import com.project.demo.logic.entity.email.EmailManager;
import com.project.demo.logic.entity.email.EmailTemplates;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador REST para la gestión de usuarios.
 * Permite crear, consultar, actualizar, eliminar usuarios y gestionar contraseñas.
 */
@RestController
@RequestMapping("/users")
public class UserRestController {

    @Value("${app.login.url}")
    private String loginUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private EmailManager emailManager;

    /**
     * Obtiene todos los usuarios paginados.
     * @param page número de página
     * @param size tamaño de página
     * @param request petición HTTP
     * @return lista de usuarios
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(userPage.getTotalPages());
        meta.setTotalElements(userPage.getTotalElements());
        meta.setPageNumber(userPage.getNumber() + 1);
        meta.setPageSize(userPage.getSize());

        return new GlobalResponseHandler().handleResponse("Usuarios obtenidos con éxito",
                userPage.getContent(), HttpStatus.OK, meta);
    }

    /**
     * Crea un nuevo usuario.
     * @param user datos del usuario
     * @param request petición HTTP
     * @return usuario creado
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody User user, HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new GlobalResponseHandler().handleResponse("Usuario creado con éxito",
                user, HttpStatus.OK, request);
    }

    /**
     * Actualiza los datos de un usuario.
     * @param userId identificador del usuario
     * @param user datos actualizados
     * @param request petición HTTP
     * @return usuario actualizado
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER','SUPER_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            User updatedUser = foundUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setLastname(user.getLastname());
            updatedUser.setProfilePic(user.getProfilePic());
            userRepository.save(updatedUser);
            return new GlobalResponseHandler().handleResponse("Usuario actualizado con éxito",
                    updatedUser, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Usuario " + userId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Actualiza los datos de un usuario por un administrador.
     * @param userId identificador del usuario
     * @param user datos actualizados
     * @param request petición HTTP
     * @return usuario actualizado
     */
    @PutMapping("/administrative/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER','SUPER_ADMIN')")
    public ResponseEntity<?> updateUserByAdmin(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            User updatedUser = foundUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setLastname(user.getLastname());
            updatedUser.setProfilePic(user.getProfilePic());
            userRepository.save(updatedUser);
            return new GlobalResponseHandler().handleResponse("Usuario actualizado con éxito",
                    updatedUser, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Usuario " + userId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Actualiza la contraseña de un usuario.
     * @param userId identificador del usuario
     * @param user datos con la nueva contraseña
     * @param request petición HTTP
     * @return usuario actualizado
     */
    @PutMapping("/password/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            User updatedUser = foundUser.get();
            updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            updatedUser.setNeedsPasswordChange(false);
            userRepository.save(updatedUser);
            return new GlobalResponseHandler().handleResponse("Contraseña actualizada con éxito",
                    updatedUser, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Usuario " + userId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Recupera la contraseña de un usuario y envía una temporal por correo.
     * @param userEmail correo del usuario
     * @param request petición HTTP
     * @return usuario actualizado
     */
    @PutMapping("/password-recovery/{userEmail}")
    public ResponseEntity<?> updatePasswordRecovery(@PathVariable String userEmail, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findByEmail(userEmail);
        if (foundUser.isPresent()) {
            User updatedUser = foundUser.get();
            String randomPassword = passwordGenerator.generatePassword(12);
            updatedUser.setPassword(passwordEncoder.encode(randomPassword));
            updatedUser.setNeedsPasswordChange(true);

            String emailBody = EmailTemplates.temporaryPasswordEmail(updatedUser.getName(), updatedUser.getEmail(), randomPassword, loginUrl);
            emailManager.sendEmail(updatedUser.getEmail(), "Generación de Contraseña Temporal", emailBody);

            userRepository.save(updatedUser);
            return new GlobalResponseHandler().handleResponse("Contraseña actualizada con éxito",
                    updatedUser, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Usuario " + userEmail + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Elimina un usuario por su identificador.
     * @param userId identificador del usuario
     * @param request petición HTTP
     * @return resultado de la eliminación
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER','SUPER_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            userRepository.deleteById(userId);
            return new GlobalResponseHandler().handleResponse("Usuario eliminado con éxito",
                    foundUser.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Usuario " + userId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Obtiene el usuario autenticado actual.
     * @return usuario autenticado
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    /**
     * Obtiene un usuario por su identificador.
     * @param userId identificador del usuario
     * @param request petición HTTP
     * @return usuario encontrado
     */
    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserById(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            return new GlobalResponseHandler().handleResponse(
                    "Usuario obtenido con éxito",
                    foundUser.get(),
                    HttpStatus.OK,
                    request
            );
        } else {
            return new GlobalResponseHandler().handleResponse(
                    "Usuario " + userId + " no encontrado",
                    HttpStatus.NOT_FOUND,
                    request
            );
        }
    }


}