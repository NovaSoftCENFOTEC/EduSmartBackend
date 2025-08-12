package com.project.demo.logic.entity.quiz;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad Quiz.
 * Permite consultar cuestionarios por historia, fecha y título.
 */
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    /**
     * Busca los cuestionarios asociados a una historia por su identificador.
     *
     * @param storyId identificador de la historia
     * @return lista de cuestionarios
     */
    List<Quiz> findByStoryId(Integer storyId);

    /**
     * Busca los cuestionarios cuya fecha de entrega es posterior a la fecha indicada.
     *
     * @param now fecha actual
     * @return lista de cuestionarios
     */
    List<Quiz> findByDueDateAfter(java.util.Date now);

    /**
     * Busca los cuestionarios que contienen un texto específico en el título (ignorando mayúsculas/minúsculas).
     *
     * @param title texto a buscar en el título
     * @return lista de cuestionarios
     */
    List<Quiz> findByTitleContainingIgnoreCase(String title);

}