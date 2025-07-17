package com.project.demo.rest.user;

import com.project.demo.logic.entity.auth.PasswordGenerator;
import com.project.demo.logic.entity.email.EmailManager;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private EmailManager emailManager;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<User> userPage = userRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(userPage.getTotalPages());
        meta.setTotalElements(userPage.getTotalElements());
        meta.setPageNumber(userPage.getNumber() + 1);
        meta.setPageSize(userPage.getSize());

        return new GlobalResponseHandler().handleResponse("Usuarios obtenidos con exito",
                userPage.getContent(), HttpStatus.OK, meta);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody User user, HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new GlobalResponseHandler().handleResponse("Usuario creado con exito",
                user, HttpStatus.OK, request);
    }

    @PutMapping("/administrative/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER','SUPER_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if(foundUser.isPresent()) {
            User updatedUser = foundUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setLastname(user.getLastname());
            userRepository.save(updatedUser);
            return new GlobalResponseHandler().handleResponse("Usuario actualizado con exito",
                    updatedUser, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Usuario " + userId + " no encontrado"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PutMapping("/password/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if(foundUser.isPresent()) {
            User updatedUser = foundUser.get();
            updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            updatedUser.setNeedsPasswordChange(false);
            userRepository.save(updatedUser);
            return new GlobalResponseHandler().handleResponse("Contraseña actualizada con exito",
                    updatedUser, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Usuario " + userId + " no encontrado"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PutMapping("/password-recovery/{userEmail}")
    public ResponseEntity<?> updatePasswordRecovery(@PathVariable String userEmail, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findByEmail(userEmail);
        if(foundUser.isPresent()) {
            User updatedUser = foundUser.get();
            String randomPassword = passwordGenerator.generatePassword(12);
            updatedUser.setPassword(passwordEncoder.encode(randomPassword));
            updatedUser.setNeedsPasswordChange(true);

            String emailBody = "Hola " + updatedUser.getName() + ",\n\n" +
                    "Se ha generado una contraseña temporal con éxito. Aquí están tus credenciales:\n" +
                    "Correo: " + updatedUser.getEmail() + "\n" +
                    "Contraseña: " + randomPassword + "\n\n" +
                    "Por favor, cambia tu contraseña al iniciar sesión usando la dirección http://localhost:4200/login.\n\n" +
                    "Saludos,\nEl equipo de EduSmart";
            emailManager.sendEmail(updatedUser.getEmail(), "Generación de Contraseña Temporal", emailBody);

            userRepository.save(updatedUser);
            return new GlobalResponseHandler().handleResponse("Contraseña actualizada con exito",
                    updatedUser, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Usuario " + userEmail + " no encontrado"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }



    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER','SUPER_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if(foundUser.isPresent()) {
            userRepository.deleteById(userId);
            return new GlobalResponseHandler().handleResponse("Usuario eliminado con exito",
                    foundUser.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Usuario " + userId + " no encontrado"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

}