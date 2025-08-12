package com.project.demo.logic.entity.material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Material.
 * Permite consultar materiales por curso y por profesor.
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    /**
     * Busca los materiales por el identificador del curso.
     * @param courseId identificador del curso
     * @param pageable paginación
     * @return página de materiales
     */
    Page<Material> findMaterialsByCourseId(Long courseId, Pageable pageable);

    /**
     * Busca los materiales por el identificador del profesor.
     * @param teacherId identificador del profesor
     * @param pageable paginación
     * @return página de materiales
     */
    Page<Material> findMaterialsByTeacherId(Long teacherId, Pageable pageable);
}
