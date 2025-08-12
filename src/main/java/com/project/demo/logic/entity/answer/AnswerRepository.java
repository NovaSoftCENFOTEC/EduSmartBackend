package com.project.demo.logic.entity.answer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Answer.
 * Proporciona métodos para consultar respuestas por entrega y pregunta.
 */
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    /**
     * Busca todas las respuestas asociadas a una entrega específica.
     *
     * @param submissionId identificador de la entrega
     * @return lista de respuestas
     */
    List<Answer> findBySubmissionId(Integer submissionId);

    /**
     * Busca una respuesta por entrega y pregunta.
     *
     * @param submissionId identificador de la entrega
     * @param questionId   identificador de la pregunta
     * @return respuesta encontrada (opcional)
     */
    Optional<Answer> findBySubmissionIdAndQuestionId(Integer submissionId, Integer questionId);

    /**
     * Busca todas las respuestas asociadas a una pregunta específica.
     *
     * @param questionId identificador de la pregunta
     * @return lista de respuestas
     */
    List<Answer> findByQuestionId(Integer questionId);
}