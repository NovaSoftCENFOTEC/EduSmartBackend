package com.project.demo.rest.user;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.school.School;
import com.project.demo.logic.entity.school.SchoolRepository;
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
    private SchoolRepository schoolRepository;

    @Autowired
    private RoleRepository roleRepository;

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

        return new GlobalResponseHandler().handleResponse("Users retrieved successfully",
                userPage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/school/{schoolId}/teachers")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> getTeachersBySchoolId(@PathVariable Long schoolId,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   HttpServletRequest request) {

        Optional<School> foundSchool = schoolRepository.findById(schoolId);
        if (foundSchool.isPresent()) {
            Optional<Role> foundRole = roleRepository.findByName(RoleEnum.valueOf("TEACHER"));
            if (foundRole.isPresent()) {

                Pageable pageable = PageRequest.of(page-1, size);
                Page<User> userPage = userRepository.findBySchoolIdAndRoleId(schoolId, foundRole.get().getId(), pageable);
                Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
                meta.setTotalPages(userPage.getTotalPages());
                meta.setTotalElements(userPage.getTotalElements());
                meta.setPageNumber(userPage.getNumber() + 1);
                meta.setPageSize(userPage.getSize());

                return new GlobalResponseHandler().handleResponse("Teachers retrieved successfully by school id",
                        userPage.getContent(), HttpStatus.OK, meta);
            } else {
                return new GlobalResponseHandler().handleResponse("Role TEACHER not found",
                        HttpStatus.NOT_FOUND, request);
            }
        } else {
            return new GlobalResponseHandler().handleResponse("School id " + schoolId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody User user, HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new GlobalResponseHandler().handleResponse("User updated successfully",
                user, HttpStatus.OK, request);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest request) {
        Optional<User> foundOrder = userRepository.findById(userId);
        if(foundOrder.isPresent()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return new GlobalResponseHandler().handleResponse("User updated successfully",
                    user, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }


    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Optional<User> foundOrder = userRepository.findById(userId);
        if(foundOrder.isPresent()) {
            userRepository.deleteById(userId);
            return new GlobalResponseHandler().handleResponse("User deleted successfully",
                    foundOrder.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Order id " + userId + " not found"  ,
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