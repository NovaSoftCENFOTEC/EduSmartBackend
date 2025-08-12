package com.project.demo.rest.badge.dto;

import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.user.User;

import java.util.Set;

/**
 * DTO que representa una insignia y sus estudiantes asociados.
 */
public class BadgeDTO {
    private Long id;
    private String title;
    private String description;
    private String iconUrl;
    private Set<User> students;

    /**
     * Constructor por defecto.
     */
    public BadgeDTO() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param id          identificador de la insignia
     * @param title       título
     * @param description descripción
     * @param iconUrl     URL del icono
     * @param students    conjunto de estudiantes
     */
    public BadgeDTO(Long id, String title, String description, String iconUrl, Set<User> students) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.iconUrl = iconUrl;
        this.students = students;
    }

    /**
     * Crea un BadgeDTO a partir de una entidad Badge.
     *
     * @param badge entidad Badge
     * @return BadgeDTO
     */
    public static BadgeDTO fromEntity(Badge badge) {
        return new BadgeDTO(
                badge.getId(),
                badge.getTitle(),
                badge.getDescription(),
                badge.getIconUrl(),
                badge.getStudents()
        );
    }

    /**
     * Obtiene el identificador de la insignia.
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador de la insignia.
     *
     * @param id identificador
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el título de la insignia.
     *
     * @return título
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título de la insignia.
     *
     * @param title título
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene la descripción de la insignia.
     *
     * @return descripción
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción de la insignia.
     *
     * @param description descripción
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene la URL del icono de la insignia.
     *
     * @return URL del icono
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * Establece la URL del icono de la insignia.
     *
     * @param iconUrl URL del icono
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * Obtiene el conjunto de estudiantes que tienen la insignia.
     *
     * @return conjunto de estudiantes
     */
    public Set<User> getStudents() {
        return students;
    }

    /**
     * Establece el conjunto de estudiantes que tienen la insignia.
     *
     * @param students conjunto de estudiantes
     */
    public void setStudents(Set<User> students) {
        this.students = students;
    }
}
