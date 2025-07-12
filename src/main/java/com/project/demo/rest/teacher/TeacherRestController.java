package com.project.demo.rest.teacher;

import com.project.demo.logic.entity.auth.PasswordGenerator;
import com.project.demo.logic.entity.email.EmailManager;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/teachers")
@RestController
public class TeacherRestController {

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

    @Autowired
    private EmailManager emailManager;

    @GetMapping("/school/{schoolId}/teachers")
    @PreAuthorize("hasAnyRole('TEACHER','SUPER_ADMIN')")
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

                return new GlobalResponseHandler().handleResponse("Profesores encontrados por ID escuela",
                        userPage.getContent(), HttpStatus.OK, meta);
            } else {
                return new GlobalResponseHandler().handleResponse("Rol no encontrado",
                        HttpStatus.NOT_FOUND, request);
            }
        } else {
            return new GlobalResponseHandler().handleResponse("Escuela " + schoolId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping("/school/{schoolId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> createTeacher(@PathVariable Long schoolId, @RequestBody User newTeacherUser, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findByEmail(newTeacherUser.getEmail());

        if (foundUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Correo se encuentra registrado");
        }

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.TEACHER);

        if (optionalRole.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rol no encontrado");
        }

        Optional<School> foundSchool = schoolRepository.findById(schoolId);

        if (foundSchool.isPresent()) {
            String randomPassword = passwordGenerator.generatePassword(12);
            newTeacherUser.setPassword(passwordEncoder.encode(randomPassword));
            newTeacherUser.setRole(optionalRole.get());
            newTeacherUser.setSchool(foundSchool.get());
            userRepository.save(newTeacherUser);

            // Send email to the new teacher
            String emailBody = "Hola " + newTeacherUser.getName() + ",\n\n" +
                    "Tu cuenta ha sido creada con éxito. Aquí están tus credenciales:\n" +
                    "Email: " + newTeacherUser.getEmail() + "\n" +
                    "Contraseña: " + randomPassword + "\n\n" +
                    "Por favor, cambia tu contraseña al iniciar sesión por primera vez usando la dirección http://localhost:4200/login.\n\n" +
                    "Saludos,\nEl equipo de EduSmart";
            emailManager.sendEmail(newTeacherUser.getEmail(), "Bienvenido a EduSmart", emailBody);

            return new GlobalResponseHandler().handleResponse("Docente creado con exito",
                    randomPassword, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Escuela " + schoolId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }
}
