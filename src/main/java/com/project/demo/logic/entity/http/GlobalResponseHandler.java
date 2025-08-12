package com.project.demo.logic.entity.http;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de respuestas HTTP.
 * Proporciona métodos para construir respuestas estándar con metadatos.
 */
@RestControllerAdvice
public class GlobalResponseHandler {
    /**
     * Construye una respuesta HTTP con mensaje, cuerpo, estado y metadatos extraídos de la petición.
     *
     * @param message mensaje de la respuesta
     * @param body    cuerpo de la respuesta
     * @param status  estado HTTP
     * @param request petición HTTP
     * @param <T>     tipo de dato del cuerpo
     * @return ResponseEntity con la respuesta
     */
    @ResponseBody
    public <T> ResponseEntity<?> handleResponse(String message, T body, HttpStatus status, HttpServletRequest request) {
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        if (body instanceof HttpResponse) {
            HttpResponse<?> response = (HttpResponse<?>) body;
            response.setMeta(meta);
            return new ResponseEntity<>(response, status);
        }
        HttpResponse<T> response = new HttpResponse<>(message, body, meta);
        return new ResponseEntity<>(response, status);
    }

    /**
     * Construye una respuesta HTTP con mensaje y estado, usando metadatos de la petición.
     *
     * @param message mensaje de la respuesta
     * @param status  estado HTTP
     * @param request petición HTTP
     * @param <T>     tipo de dato del cuerpo
     * @return ResponseEntity con la respuesta
     */
    @ResponseBody
    public <T> ResponseEntity<?> handleResponse(String message, HttpStatus status, HttpServletRequest request) {
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        HttpResponse<?> response = new HttpResponse<>(message, meta);
        return new ResponseEntity<>(response, status);
    }

    /**
     * Construye una respuesta HTTP con mensaje, cuerpo, estado y metadatos proporcionados.
     *
     * @param message mensaje de la respuesta
     * @param body    cuerpo de la respuesta
     * @param status  estado HTTP
     * @param meta    metadatos
     * @param <T>     tipo de dato del cuerpo
     * @return ResponseEntity con la respuesta
     */
    @ResponseBody
    public <T> ResponseEntity<?> handleResponse(String message, T body, HttpStatus status, Meta meta) {
        if (body instanceof HttpResponse) {
            HttpResponse<?> response = (HttpResponse<?>) body;
            response.setMeta(meta);
            return new ResponseEntity<>(response, status);
        }
        HttpResponse<T> response = new HttpResponse<>(message, body, meta);
        return new ResponseEntity<>(response, status);
    }
}
