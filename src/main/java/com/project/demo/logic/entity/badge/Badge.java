package com.project.demo.logic.entity.badge;

import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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

    public Badge() {
    }
    public Badge(Long id, String title, String description, String iconUrl, Set<User> students) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.iconUrl = iconUrl;
        this.students = students;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Set<User> getStudents() {
        return students;
    }

    public void setStudents(Set<User> students) {
        this.students = students;
    }
}
