package com.project.demo.logic.entity.rol;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * Seeder para cargar los roles básicos en la base de datos al iniciar la aplicación.
 */
@Order(1)
@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;


    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Método que se ejecuta al iniciar el contexto de la aplicación.
     * Carga los roles si no existen.
     *
     * @param contextRefreshedEvent evento de inicio de contexto
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();
    }

    /**
     * Carga los roles básicos en la base de datos si no existen.
     */
    private void loadRoles() {
        RoleEnum[] roleNames = new RoleEnum[]{RoleEnum.STUDENT, RoleEnum.TEACHER, RoleEnum.SUPER_ADMIN};
        Map<RoleEnum, String> roleDescriptionMap = Map.of(
                RoleEnum.STUDENT, "Student role",
                RoleEnum.TEACHER, "Teacher role",
                RoleEnum.SUPER_ADMIN, "Super Administrator role"
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);

            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role roleToCreate = new Role();

                roleToCreate.setName(roleName);
                roleToCreate.setDescription(roleDescriptionMap.get(roleName));

                roleRepository.save(roleToCreate);
            });
        });
    }
}
