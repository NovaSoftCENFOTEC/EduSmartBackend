package com.project.demo.rest.badge.dto;

import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.user.User;

import java.util.Set;

public class BadgeDTO {
    private Long id;
    private String title;
    private String description;
    private String iconUrl;
    private Set<User> students;

    public BadgeDTO() {}

    public BadgeDTO(Long id, String title, String description, String iconUrl, Set<User> students) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.iconUrl = iconUrl;
        this.students = students;
    }

    public static BadgeDTO fromEntity(Badge badge) {
        return new BadgeDTO(
                badge.getId(),
                badge.getTitle(),
                badge.getDescription(),
                badge.getIconUrl(),
                badge.getStudents()
        );
    }

    // getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public Set<User> getStudents() { return students; }
    public void setStudents(Set<User> students) { this.students = students; }
}
