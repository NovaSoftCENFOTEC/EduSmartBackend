package com.project.demo.logic.entity.grade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Grade.
 * Permite consultar calificaciones por entrega y por estudiante.
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    /**
     * Busca las calificaciones por el identificador de la entrega.
     *
     * @param submissionId identificador de la entrega
     * @param pageable     paginación
     * @return página de calificaciones
     */
    Page<Grade> findBySubmissionId(Long submissionId, Pageable pageable);

    /**
     * Busca las calificaciones por el identificador del estudiante.
     *
     * @param studentId identificador del estudiante
     * @param pageable  paginación
     * @return página de calificaciones
     */
    Page<Grade> findBySubmissionStudentId(Long studentId, Pageable pageable);
}
