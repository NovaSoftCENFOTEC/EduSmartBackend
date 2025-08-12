package com.project.demo.logic.entity.story;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Story.
 * Permite consultar historias por curso.
 */
@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    /**
     * Busca las historias asociadas a un curso por su identificador.
     *
     * @param courseId identificador del curso
     * @param pageable paginación
     * @return página de historias
     */
    Page<Story> findStoriesByCourseId(Long courseId, Pageable pageable);
}
