package com.project.demo.logic.entity.assignment;

import com.project.demo.logic.entity.group.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Assignment.
 * Proporciona métodos para consultar tareas por grupo.
 */
@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    /**
     * Busca tareas por el identificador del grupo.
     * @param groupId identificador del grupo
     * @param pageable paginación
     * @return página de tareas
     */
    Page<Assignment> findByGroupId(Long groupId, Pageable pageable);

    /**
     * Busca tareas por el grupo.
     * @param group grupo
     * @param pageable paginación
     * @return página de tareas
     */
    Page<Assignment> findByGroup(Group group, Pageable pageable);
}
