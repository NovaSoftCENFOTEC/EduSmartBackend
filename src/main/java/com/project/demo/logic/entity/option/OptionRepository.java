package com.project.demo.logic.entity.option;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la entidad Option.
 * Permite consultar opciones por pregunta y obtener la opción correcta.
 */
public interface OptionRepository extends JpaRepository<Option, Integer> {
    /**
     * Busca las opciones asociadas a una pregunta por su identificador.
     *
     * @param questionId identificador de la pregunta
     * @return lista de opciones
     */
    List<Option> findByQuestionId(Integer questionId);

    /**
     * Busca la opción correcta asociada a una pregunta.
     *
     * @param questionId identificador de la pregunta
     * @return opción correcta
     */
    Option findByQuestionIdAndIsCorrectTrue(Integer questionId);
}