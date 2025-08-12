package com.project.demo.logic.entity.rol;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * Representa un rol de usuario dentro del sistema.
 * Contiene información como nombre, descripción y fechas de creación/actualización.
 */
@Table(name = "role")
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * Obtiene el identificador único del rol.
     *
     * @return id del rol
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador único del rol.
     *
     * @param id identificador del rol
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del rol.
     *
     * @return nombre del rol
     */
    public RoleEnum getName() {
        return name;
    }

    /**
     * Establece el nombre del rol.
     *
     * @param name nombre del rol
     */
    public void setName(RoleEnum name) {
        this.name = name;
    }

    /**
     * Obtiene la descripción del rol.
     *
     * @return descripción
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción del rol.
     *
     * @param description descripción
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene la fecha de creación del rol.
     *
     * @return fecha de creación
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha de creación del rol.
     *
     * @param createdAt fecha de creación
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Obtiene la fecha de actualización del rol.
     *
     * @return fecha de actualización
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Establece la fecha de actualización del rol.
     *
     * @param updatedAt fecha de actualización
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
