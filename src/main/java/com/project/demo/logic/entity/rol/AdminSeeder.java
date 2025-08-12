package com.project.demo.logic.entity.rol;

import com.project.demo.logic.entity.school.School;
import com.project.demo.logic.entity.school.SchoolRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Seeder para crear el usuario Super Administrador al iniciar la aplicación.
 */
@Order(3)
@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;

    private final PasswordEncoder passwordEncoder;


    public AdminSeeder(
            RoleRepository roleRepository,
            UserRepository  userRepository,
            SchoolRepository schoolRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Método que se ejecuta al iniciar el contexto de la aplicación.
     * Crea el usuario Super Administrador si no existe.
     * @param contextRefreshedEvent evento de inicio de contexto
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createSuperAdministrator();
    }

    /**
     * Crea el usuario Super Administrador en la base de datos si no existe.
     */
    private void createSuperAdministrator() {
        User superAdmin = new User();
        superAdmin.setName("Super");
        superAdmin.setLastname("Admin");
        superAdmin.setEmail("super.admin@gmail.com");
        superAdmin.setPassword("superadmin123");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
        Optional<User> optionalUser = userRepository.findByEmail(superAdmin.getEmail());
        Optional<School> optionalSchool = schoolRepository.findByDomain("DEFAULT");

        if (optionalRole.isEmpty() || optionalUser.isPresent() || optionalSchool.isEmpty()) {
            return;
        }

        var user = new User();
        user.setName(superAdmin.getName());
        user.setLastname(superAdmin.getLastname());
        user.setEmail(superAdmin.getEmail());
        user.setPassword(passwordEncoder.encode(superAdmin.getPassword()));
        user.setRole(optionalRole.get());
        user.setSchool(optionalSchool.get());

        userRepository.save(user);
    }
}
