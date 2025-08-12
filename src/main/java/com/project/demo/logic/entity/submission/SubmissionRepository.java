package com.project.demo.logic.entity.submission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Submission.
 * Permite consultar entregas por cuestionario y por estudiante.
 */
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

    /**
     * Busca una entrega por el identificador del cuestionario y del estudiante.
     *
     * @param quizId    identificador del cuestionario
     * @param studentId identificador del estudiante
     * @return entrega encontrada (opcional)
     */
    Optional<Submission> findByQuizIdAndStudentId(Integer quizId, Integer studentId);

    /**
     * Busca las entregas por el identificador del cuestionario.
     *
     * @param quizId identificador del cuestionario
     * @return lista de entregas
     */
    List<Submission> findByQuizId(Integer quizId);

    /**
     * Busca las entregas por el identificador del estudiante.
     *
     * @param studentId identificador del estudiante
     * @return lista de entregas
     */
    List<Submission> findByStudentId(Integer studentId);
}