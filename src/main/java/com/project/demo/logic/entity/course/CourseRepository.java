package com.project.demo.logic.entity.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Course.
 * Permite realizar operaciones CRUD sobre los cursos.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
