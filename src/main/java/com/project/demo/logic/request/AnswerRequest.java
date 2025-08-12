package com.project.demo.logic.request;

/**
 * Solicitud para registrar una respuesta a una pregunta.
 * Contiene el identificador de la pregunta y la opción seleccionada.
 */
public class AnswerRequest {
    private Integer questionId;
    private Integer optionId;

    /**
     * Obtiene el identificador de la pregunta.
     * @return id de la pregunta
     */
    public Integer getQuestionId() { return questionId; }

    /**
     * Establece el identificador de la pregunta.
     * @param questionId id de la pregunta
     */
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }

    /**
     * Obtiene el identificador de la opción seleccionada.
     * @return id de la opción
     */
    public Integer getOptionId() { return optionId; }

    /**
     * Establece el identificador de la opción seleccionada.
     * @param optionId id de la opción
     */
    public void setOptionId(Integer optionId) { this.optionId = optionId; }
}