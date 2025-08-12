package com.project.demo.logic.request;

/**
 * Solicitud para actualizar la opción seleccionada en una respuesta.
 */
public class AnswerUpdateRequest {
    private Integer optionId;

    /**
     * Obtiene el identificador de la nueva opción seleccionada.
     * @return id de la opción
     */
    public Integer getOptionId() {
        return optionId;
    }

    /**
     * Establece el identificador de la nueva opción seleccionada.
     * @param optionId id de la opción
     */
    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }
}