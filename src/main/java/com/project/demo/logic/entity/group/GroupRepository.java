package com.project.demo.logic.entity.group;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Group.
 * Permite consultar grupos por curso, profesor y estudiante.
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    /**
     * Busca los grupos por el identificador del curso.
     * @param courseId identificador del curso
     * @param pageable paginación
     * @return página de grupos
     */
    Page<Group> findGroupsByCourseId(Long courseId, Pageable pageable);

    /**
     * Busca los grupos por el identificador del profesor.
     * @param teacherId identificador del profesor
     * @param pageable paginación
     * @return página de grupos
     */
    Page<Group> findGroupsByTeacherId(Long teacherId, Pageable pageable);

    /**
     * Busca un grupo junto con sus estudiantes por el identificador del grupo.
     * @param groupId identificador del grupo
     * @return grupo con estudiantes (opcional)
     */
    Optional<Group> findWithStudentsById(Long groupId);

    /**
     * Busca los grupos por el identificador del estudiante.
     * @param studentId identificador del estudiante
     * @param pageable paginación
     * @return página de grupos
     */
    @Query("SELECT DISTINCT g FROM Group g JOIN g.students s WHERE s.id = :studentId")
    Page<Group> findGroupsByStudentId(@Param("studentId") Long studentId, Pageable pageable);
}
