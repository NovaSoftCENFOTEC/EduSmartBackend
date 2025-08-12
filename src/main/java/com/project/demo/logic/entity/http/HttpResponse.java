package com.project.demo.logic.entity.http;


/**
 * Representa una respuesta HTTP estándar.
 * Incluye mensaje, datos y metadatos de la respuesta.
 *
 * @param <T> tipo de dato de la respuesta
 */
public class HttpResponse<T> {
    private String message;
    private T data;
    private Meta meta;

    /**
     * Constructor con solo mensaje.
     *
     * @param message mensaje de la respuesta
     */
    public HttpResponse(String message) {
        this.message = message;
    }

    /**
     * Constructor con mensaje y datos.
     *
     * @param message mensaje de la respuesta
     * @param data    datos de la respuesta
     */
    public HttpResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    /**
     * Constructor con mensaje y metadatos.
     *
     * @param message mensaje de la respuesta
     * @param meta    metadatos
     */
    public HttpResponse(String message, Meta meta) {
        this.message = message;
        this.meta = meta;
    }

    /**
     * Constructor con mensaje, datos y metadatos.
     *
     * @param message mensaje de la respuesta
     * @param data    datos de la respuesta
     * @param meta    metadatos
     */
    public HttpResponse(String message, T data, Meta meta) {
        this.message = message;
        this.data = data;
        this.meta = meta;
    }

    /**
     * Obtiene el mensaje de la respuesta.
     *
     * @return mensaje
     */
    public String getMessage() {
        return message;
    }

    /**
     * Establece el mensaje de la respuesta.
     *
     * @param message mensaje
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Obtiene los datos de la respuesta.
     *
     * @return datos
     */
    public T getData() {
        return data;
    }

    /**
     * Establece los datos de la respuesta.
     *
     * @param data datos
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Obtiene los metadatos de la respuesta.
     *
     * @return metadatos
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * Establece los metadatos de la respuesta.
     *
     * @param meta metadatos
     */
    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
