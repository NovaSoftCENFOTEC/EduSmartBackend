package com.project.demo.logic.entity.badge;

import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Representa una insignia que puede ser otorgada a los usuarios.
 * Contiene información como título, descripción, icono y los estudiantes que la poseen.
 */
@Table(name = "badge")
@Entity
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Column(name = "icon_url")
    private String iconUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "badge_user",
            joinColumns = @JoinColumn(name = "badge_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<User> students = new HashSet<>();

    /**
     * Constructor por defecto.
     */
    public Badge() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param id          identificador de la insignia
     * @param title       título de la insignia
     * @param description descripción de la insignia
     * @param iconUrl     URL del icono
     * @param students    conjunto de estudiantes que poseen la insignia
     */
    public Badge(Long id, String title, String description, String iconUrl, Set<User> students) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.iconUrl = iconUrl;
        this.students = students;
    }

    /**
     * Obtiene el identificador único de la insignia.
     *
     * @return id de la insignia
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la insignia.
     *
     * @param id identificador de la insignia
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
     * Obtiene el conjunto de estudiantes que poseen la insignia.
     *
     * @return conjunto de estudiantes
     */
    public Set<User> getStudents() {
        return students;
    }

    /**
     * Establece el conjunto de estudiantes que poseen la insignia.
     *
     * @param students conjunto de estudiantes
     */
    public void setStudents(Set<User> students) {
        this.students = students;
    }
}
