package com.project.demo.logic.entity.school;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Seeder para crear la escuela predeterminada al iniciar la aplicación.
 */
@Order(2)
@Component
public class SchoolSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final SchoolRepository schoolRepository;

    public SchoolSeeder(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    /**
     * Método que se ejecuta al iniciar el contexto de la aplicación.
     * Crea la escuela predeterminada si no existe.
     *
     * @param contextRefreshedEvent evento de inicio de contexto
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createDefaultSchool();
    }

    /**
     * Crea la escuela predeterminada en la base de datos si no existe.
     */
    private void createDefaultSchool() {
        School defaultSchool = new School();
        defaultSchool.setName("Escuela Predeterminada");
        defaultSchool.setDomain("DEFAULT");

        Optional<School> optionalSchool = schoolRepository.findByDomain("DEFAULT");
        if (optionalSchool.isEmpty()) {
            schoolRepository.save(defaultSchool);
        }
    }
}
