package com.project.demo.logic.entity.audioTrack;

import com.project.demo.logic.entity.story.Story;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Table(name = "audio_track")
@Entity
public class AudioTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    @Column(unique = true, nullable = false, name = "voice_type")
    @Enumerated(EnumType.STRING)
    private VoiceTypeEnum voiceType;
    private String url;
    @Column(name = "duration_sec")
    private int duration; // in seconds

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "story_id", referencedColumnName = "id", nullable = false)
    private Story story;

    public AudioTrack() {
    }

    public AudioTrack(long id, String title, VoiceTypeEnum voiceType, String url, int duration, Date createdAt, Story story) {
        this.id = id;
        this.title = title;
        this.voiceType = voiceType;
        this.url = url;
        this.duration = duration;
        this.createdAt = createdAt;
        this.story = story;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public VoiceTypeEnum getVoiceType() {
        return voiceType;
    }

    public void setVoiceType(VoiceTypeEnum voiceType) {
        this.voiceType = voiceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }
}
