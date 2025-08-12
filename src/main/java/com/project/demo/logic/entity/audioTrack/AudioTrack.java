package com.project.demo.logic.entity.audioTrack;

import com.project.demo.logic.entity.story.Story;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/**
 * Representa una pista de audio asociada a una historia.
 * Incluye información como título, tipo de voz, URL y fecha de creación.
 */
@Table(name = "audio_track")
@Entity
public class AudioTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    @Column( nullable = false, name = "voice_type")
    @Enumerated(EnumType.STRING)
    private VoiceTypeEnum voiceType;
    private String url;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "story_id", referencedColumnName = "id", nullable = false)
    private Story story;

    /**
     * Constructor por defecto.
     */
    public AudioTrack() {
    }

    /**
     * Constructor con todos los parámetros.
     * @param id identificador de la pista
     * @param title título de la pista
     * @param voiceType tipo de voz
     * @param url URL de la pista
     * @param createdAt fecha de creación
     * @param story historia asociada
     */
    public AudioTrack(long id, String title, VoiceTypeEnum voiceType, String url, Date createdAt, Story story) {
        this.id = id;
        this.title = title;
        this.voiceType = voiceType;
        this.url = url;
        this.createdAt = createdAt;
        this.story = story;
    }

    /**
     * Obtiene el identificador único de la pista de audio.
     * @return id de la pista
     */
    public long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la pista de audio.
     * @param id identificador de la pista
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Obtiene el título de la pista de audio.
     * @return título
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título de la pista de audio.
     * @param title título
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene el tipo de voz de la pista de audio.
     * @return tipo de voz
     */
    public VoiceTypeEnum getVoiceType() {
        return voiceType;
    }

    /**
     * Establece el tipo de voz de la pista de audio.
     * @param voiceType tipo de voz
     */
    public void setVoiceType(VoiceTypeEnum voiceType) {
        this.voiceType = voiceType;
    }

    /**
     * Obtiene la URL de la pista de audio.
     * @return URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Establece la URL de la pista de audio.
     * @param url URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Obtiene la fecha de creación de la pista de audio.
     * @return fecha de creación
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha de creación de la pista de audio.
     * @param createdAt fecha de creación
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Obtiene la historia asociada a la pista de audio.
     * @return historia
     */
    public Story getStory() {
        return story;
    }

    /**
     * Establece la historia asociada a la pista de audio.
     * @param story historia
     */
    public void setStory(Story story) {
        this.story = story;
    }
}
