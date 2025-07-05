package com.project.demo.rest.teacher;

import com.project.demo.logic.entity.auth.PasswordGenerator;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.school.School;
import com.project.demo.logic.entity.school.SchoolRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/teachers")
@RestController
public class TeacherController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @PostMapping("/school/{schoolId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> createTeacher(@PathVariable Long schoolId, @RequestBody User newTeacherUser, HttpServletRequest request) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.TEACHER);
        Optional<School> foundSchool = schoolRepository.findById(schoolId);

        if (optionalRole.isEmpty()) {
            return null;
        }

        if (foundSchool.isPresent()) {
            String randomPassword = passwordGenerator.generatePassword(8);
            newTeacherUser.setPassword(passwordEncoder.encode(randomPassword));
            newTeacherUser.setRole(optionalRole.get());
            newTeacherUser.setSchool(foundSchool.get());
            userRepository.save(newTeacherUser);
            return new GlobalResponseHandler().handleResponse("Teacher created successfully",
                    randomPassword, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("School id " + schoolId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }
}
