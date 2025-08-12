package com.project.demo.logic.entity.school;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad School.
 * Permite consultar escuelas por dominio.
 */
@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    /**
     * Busca una escuela por su dominio.
     *
     * @param domain dominio de la escuela
     * @return escuela encontrada (opcional)
     */
    Optional<School> findByDomain(String domain);
}
