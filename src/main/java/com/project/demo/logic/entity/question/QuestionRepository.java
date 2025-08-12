package com.project.demo.logic.entity.question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio para la entidad Question.
 * Permite consultar preguntas por cuestionario y por texto.
 */
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    /**
     * Busca las preguntas asociadas a un cuestionario por su identificador.
     * @param quizId identificador del cuestionario
     * @return lista de preguntas
     */
    List<Question> findByQuizId(Integer quizId);

    /**
     * Busca las preguntas que contienen un texto específico (ignorando mayúsculas/minúsculas).
     * @param text texto a buscar
     * @return lista de preguntas
     */
    List<Question> findByTextContainingIgnoreCase(String text);
}