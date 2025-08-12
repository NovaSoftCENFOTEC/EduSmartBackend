package com.project.demo.logic.entity.course;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/**
 * Representa un curso dentro de la plataforma.
 * Contiene información como código, título, descripción y fecha de creación.
 */
@Table(name = "course")
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String code;
    private String title;
    private String description;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    /**
     * Constructor por defecto.
     */
    public Course() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param id          identificador del curso
     * @param code        código único del curso
     * @param title       título del curso
     * @param description descripción del curso
     * @param createdAt   fecha de creación
     */
    public Course(long id, String code, String title, String description, Date createdAt) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    /**
     * Obtiene el identificador único del curso.
     *
     * @return id del curso
     */
    public long getId() {
        return id;
    }

    /**
     * Establece el identificador único del curso.
     *
     * @param id identificador del curso
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Obtiene el código único del curso.
     *
     * @return código del curso
     */
    public String getCode() {
        return code;
    }

    /**
     * Establece el código único del curso.
     *
     * @param code código del curso
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Obtiene el título del curso.
     *
     * @return título
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título del curso.
     *
     * @param title título
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene la descripción del curso.
     *
     * @return descripción
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción del curso.
     *
     * @param description descripción
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene la fecha de creación del curso.
     *
     * @return fecha de creación
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha de creación del curso.
     *
     * @param createdAt fecha de creación
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
