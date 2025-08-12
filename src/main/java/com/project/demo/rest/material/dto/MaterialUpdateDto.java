package com.project.demo.rest.material.dto;

/**
 * DTO para actualizar los datos de un material educativo.
 * Incluye nombre, URL del archivo, curso y profesor asociados.
 */
public class MaterialUpdateDto {

    private String name;
    private String fileUrl;
    private Long courseId;
    private Long teacherId;

    /**
     * Constructor por defecto.
     */
    public MaterialUpdateDto() {
    }

    /**
     * Obtiene el nombre del material.
     * @return nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del material.
     * @param name nombre
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la URL del archivo del material.
     * @return URL del archivo
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * Establece la URL del archivo del material.
     * @param fileUrl URL del archivo
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * Obtiene el identificador del curso asociado.
     * @return id del curso
     */
    public Long getCourseId() {
        return courseId;
    }

    /**
     * Establece el identificador del curso asociado.
     * @param courseId id del curso
     */
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    /**
     * Obtiene el identificador del profesor asociado.
     * @return id del profesor
     */
    public Long getTeacherId() {
        return teacherId;
    }

    /**
     * Establece el identificador del profesor asociado.
     * @param teacherId id del profesor
     */
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}