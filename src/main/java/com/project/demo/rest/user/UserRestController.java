package com.project.demo.rest.user;

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

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        Optional<User> foundOrder = userRepository.findById(userId);
        if(foundOrder.isPresent()) {
            User updatedUser = foundOrder.get();
            updatedUser.setName(user.getName());
            updatedUser.setLastname(user.getLastname());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            updatedUser.setProfilePic(user.getProfilePic());
            updatedUser.setRole(user.getRole());
            updatedUser.setSchool(user.getSchool());

            userRepository.save(updatedUser);
            return new GlobalResponseHandler().handleResponse("Usuario actualizado con exito",
                    updatedUser, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Usuario " + userId + " no encontrado"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }


    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> foundOrder = userRepository.findById(userId);
        if(foundOrder.isPresent()) {
            userRepository.deleteById(userId);
            return new GlobalResponseHandler().handleResponse("Usuario eliminado con exito",
                    foundOrder.get(), HttpStatus.OK, request);
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