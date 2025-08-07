package com.project.demo.logic.entity.grade;

import com.project.demo.logic.entity.taskSubmission.TaskSubmission;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_grade")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double grade;

    private String justification;

    @Column(name = "graded_at")
    @CreationTimestamp
    private LocalDateTime gradedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "submission_id", referencedColumnName = "id")
    private TaskSubmission submission;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private User teacher;

    public Grade() {}

    public Grade(Double grade, String justification, TaskSubmission submission, User teacher) {
        this.grade = grade;
        this.justification = justification;
        this.submission = submission;
        this.teacher = teacher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public LocalDateTime getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }

    public TaskSubmission getSubmission() {
        return submission;
    }

    public void setSubmission(TaskSubmission submission) {
        this.submission = submission;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}

