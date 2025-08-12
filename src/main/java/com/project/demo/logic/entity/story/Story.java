package com.project.demo.logic.entity.story;

import com.project.demo.logic.entity.course.Course;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/**
 * Representa una historia educativa dentro de la plataforma.
 * Contiene información como título, contenido, fecha de creación y curso asociado.
 */
@Table(name = "story")
@Entity
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    private Course course;

    /**
     * Constructor por defecto.
     */
    public Story() {
    }

    /**
     * Constructor con todos los parámetros.
     * @param id identificador de la historia
     * @param title título de la historia
     * @param content contenido de la historia
     * @param createdAt fecha de creación
     * @param course curso asociado
     */
    public Story(long id, String title, String content, Date createdAt, Course course) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.course = course;
    }

    /**
     * Obtiene el identificador único de la historia.
     * @return id de la historia
     */
    public long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la historia.
     * @param id identificador de la historia
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Obtiene el título de la historia.
     * @return título
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título de la historia.
     * @param title título
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene el contenido de la historia.
     * @return contenido
     */
    public String getContent() {
        return content;
    }

    /**
     * Establece el contenido de la historia.
     * @param content contenido
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Obtiene la fecha de creación de la historia.
     * @return fecha de creación
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha de creación de la historia.
     * @param createdAt fecha de creación
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Obtiene el curso asociado a la historia.
     * @return curso
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Establece el curso asociado a la historia.
     * @param course curso
     */
    public void setCourse(Course course) {
        this.course = course;
    }
}
