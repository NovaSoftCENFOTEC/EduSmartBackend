package com.project.demo.rest.answer.dto;

/**
 * DTO que representa el resultado de una respuesta en un cuestionario.
 * Incluye la pregunta, la respuesta seleccionada y si fue correcta.
 */
public class AnswerResultDto {
    private String question;
    private String selectedAnswer;
    private boolean isCorrect;

    /**
     * Constructor por defecto.
     */
    public AnswerResultDto() {}

    /**
     * Constructor con todos los parámetros.
     * @param question pregunta
     * @param selectedAnswer respuesta seleccionada
     * @param isCorrect indica si la respuesta es correcta
     */
    public AnswerResultDto(String question, String selectedAnswer, boolean isCorrect) {
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
    }

    /**
     * Obtiene el texto de la pregunta.
     * @return pregunta
     */
    public String getQuestion() { return question; }

    /**
     * Establece el texto de la pregunta.
     * @param question pregunta
     */
    public void setQuestion(String question) { this.question = question; }

    /**
     * Obtiene la respuesta seleccionada.
     * @return respuesta seleccionada
     */
    public String getSelectedAnswer() { return selectedAnswer; }

    /**
     * Establece la respuesta seleccionada.
     * @param selectedAnswer respuesta seleccionada
     */
    public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }

    /**
     * Indica si la respuesta es correcta.
     * @return true si es correcta, false en caso contrario
     */
    public boolean isCorrect() { return isCorrect; }

    /**
     * Establece si la respuesta es correcta.
     * @param correct true si es correcta
     */
    public void setCorrect(boolean correct) { isCorrect = correct; }
}