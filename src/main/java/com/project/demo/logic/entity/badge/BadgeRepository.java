package com.project.demo.logic.entity.badge;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repositorio para la entidad Badge.
 * Proporciona métodos para consultar insignias por estudiante.
 */
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    /**
     * Busca las insignias asociadas a un estudiante por su identificador.
     *
     * @param studentId identificador del estudiante
     * @param pageable  paginación
     * @return página de insignias
     */
    @Query("SELECT b FROM Badge b JOIN b.students s WHERE s.id = :studentId")
    Page<Badge> findBadgesByStudentId(Long studentId, Pageable pageable);
}
