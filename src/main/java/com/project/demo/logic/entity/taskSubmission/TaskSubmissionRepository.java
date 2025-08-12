package com.project.demo.logic.entity.taskSubmission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad TaskSubmission.
 * Permite consultar entregas de tareas por estudiante y por tarea.
 */
public interface TaskSubmissionRepository extends JpaRepository<TaskSubmission, Long> {

    /**
     * Busca las entregas de tareas por el identificador del estudiante.
     *
     * @param studentId identificador del estudiante
     * @param pageable  paginación
     * @return página de entregas de tareas
     */
    Page<TaskSubmission> findByStudentId(Long studentId, Pageable pageable);

    /**
     * Busca las entregas de tareas por el identificador de la tarea.
     *
     * @param assignmentId identificador de la tarea
     * @param pageable     paginación
     * @return página de entregas de tareas
     */
    Page<TaskSubmission> findByAssignmentId(Long assignmentId, Pageable pageable);
}
