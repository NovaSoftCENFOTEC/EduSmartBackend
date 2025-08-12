package com.project.demo.logic.entity.illustration;

import com.project.demo.logic.entity.story.Story;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/**
 * Representa una ilustración asociada a una historia.
 * Incluye información como título, URL, texto alternativo y fecha de creación.
 */
@Table(name = "illustration")
@Entity
public class Illustration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String url;
    @Column(name = "alt_text")
    private String altText;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "story_id", referencedColumnName = "id", nullable = false)
    private Story story;

    /**
     * Constructor por defecto.
     */
    public Illustration() {
    }

    /**
     * Constructor con todos los parámetros.
     * @param id identificador de la ilustración
     * @param title título de la ilustración
     * @param url URL de la ilustración
     * @param altText texto alternativo
     * @param createdAt fecha de creación
     * @param story historia asociada
     */
    public Illustration(long id, String title, String url, String altText, Date createdAt, Story story) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.altText = altText;
        this.createdAt = createdAt;
        this.story = story;
    }

    /**
     * Obtiene el identificador único de la ilustración.
     * @return id de la ilustración
     */
    public long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la ilustración.
     * @param id identificador de la ilustración
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Obtiene el título de la ilustración.
     * @return título
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título de la ilustración.
     * @param title título
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene la URL de la ilustración.
     * @return URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Establece la URL de la ilustración.
     * @param url URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Obtiene el texto alternativo de la ilustración.
     * @return texto alternativo
     */
    public String getAltText() {
        return altText;
    }

    /**
     * Establece el texto alternativo de la ilustración.
     * @param altText texto alternativo
     */
    public void setAltText(String altText) {
        this.altText = altText;
    }

    /**
     * Obtiene la fecha de creación de la ilustración.
     * @return fecha de creación
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha de creación de la ilustración.
     * @param createdAt fecha de creación
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Obtiene la historia asociada a la ilustración.
     * @return historia
     */
    public Story getStory() {
        return story;
    }

    /**
     * Establece la historia asociada a la ilustración.
     * @param story historia
     */
    public void setStory(Story story) {
        this.story = story;
    }
}
