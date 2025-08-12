package com.project.demo.rest.submission.dto;

import com.project.demo.rest.answer.dto.AnswerResultDto;

import java.util.List;

/**
 * DTO que representa el resultado de una entrega de cuestionario.
 * Incluye el total de preguntas, respuestas correctas, puntaje y resultados detallados.
 */
public class SubmissionResultDto {
    private int totalQuestions;
    private int correctAnswers;
    private double score;
    private List<AnswerResultDto> results;

    /**
     * Constructor por defecto.
     */
    public SubmissionResultDto() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param totalQuestions total de preguntas
     * @param correctAnswers número de respuestas correctas
     * @param score          puntaje obtenido
     * @param results        lista de resultados detallados
     */
    public SubmissionResultDto(int totalQuestions, int correctAnswers, double score, List<AnswerResultDto> results) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.score = score;
        this.results = results;
    }

    /**
     * Obtiene el total de preguntas.
     *
     * @return total de preguntas
     */
    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    /**
     * Obtiene el número de respuestas correctas.
     *
     * @return respuestas correctas
     */
    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    /**
     * Obtiene el puntaje obtenido.
     *
     * @return puntaje
     */
    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Obtiene la lista de resultados detallados.
     *
     * @return lista de resultados
     */
    public List<AnswerResultDto> getResults() {
        return results;
    }

    public void setResults(List<AnswerResultDto> results) {
        this.results = results;
    }
}