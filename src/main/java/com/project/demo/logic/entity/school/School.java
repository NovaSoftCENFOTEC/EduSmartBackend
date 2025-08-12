package com.project.demo.logic.entity.school;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/**
 * Representa una escuela dentro de la plataforma.
 * Contiene información como nombre, dominio y fecha de creación.
 */
@Table(name = "school")
@Entity
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String domain;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    /**
     * Constructor por defecto.
     */
    public School() {
    }

    /**
     * Constructor con todos los parámetros.
     * @param id identificador de la escuela
     * @param name nombre de la escuela
     * @param domain dominio de la escuela
     * @param createdAt fecha de creación
     */
    public School(long id, String name, String domain, Date createdAt) {
        this.id = id;
        this.name = name;
        this.domain = domain;
        this.createdAt = createdAt;
    }

    /**
     * Obtiene el identificador único de la escuela.
     * @return id de la escuela
     */
    public long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la escuela.
     * @param id identificador de la escuela
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la escuela.
     * @return nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la escuela.
     * @param name nombre
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el dominio de la escuela.
     * @return dominio
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Establece el dominio de la escuela.
     * @param domain dominio
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Obtiene la fecha de creación de la escuela.
     * @return fecha de creación
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha de creación de la escuela.
     * @param createdAt fecha de creación
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
