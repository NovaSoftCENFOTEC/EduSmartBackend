package com.project.demo.logic.entity.http;

/**
 * Representa los metadatos de una respuesta HTTP.
 * Incluye información sobre el método, URL y paginación.
 */
public class Meta {
    private String method;
    private String url;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int pageSize;

    /**
     * Constructor con método y URL.
     *
     * @param method método HTTP
     * @param url    URL de la petición
     */
    public Meta(String method, String url) {
        this.method = method;
        this.url = url;
    }

    /**
     * Obtiene el método HTTP.
     *
     * @return método
     */
    public String getMethod() {
        return method;
    }

    /**
     * Establece el método HTTP.
     *
     * @param method método
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Obtiene la URL de la petición.
     *
     * @return URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Establece la URL de la petición.
     *
     * @param url URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Obtiene el número total de páginas.
     *
     * @return total de páginas
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Establece el número total de páginas.
     *
     * @param totalPages total de páginas
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Obtiene el número total de elementos.
     *
     * @return total de elementos
     */
    public long getTotalElements() {
        return totalElements;
    }

    /**
     * Establece el número total de elementos.
     *
     * @param totalElements total de elementos
     */
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * Obtiene el número de página actual.
     *
     * @return número de página
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Establece el número de página actual.
     *
     * @param pageNumber número de página
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Obtiene el tamaño de página.
     *
     * @return tamaño de página
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Establece el tamaño de página.
     *
     * @param pageSize tamaño de página
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
