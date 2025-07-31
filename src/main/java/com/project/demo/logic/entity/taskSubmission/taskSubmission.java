package com.project.demo.logic.entity.taskSubmission;
import com.project.demo.logic.entity.assignment.Assignment;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Table(name = "task_submission")
@Entity
public class taskSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_url")
    private String fileUrl;

    private String comment;

    @CreationTimestamp
    @Column(name = "submitted_at")
    private LocalDate submittedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignment_id", referencedColumnName = "id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private User student;

    public taskSubmission() {
    }

    public taskSubmission(Long id, String fileUrl, String comment, LocalDate submittedAt, Assignment assignment, User student) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.comment = comment;
        this.submittedAt = submittedAt;
        this.assignment = assignment;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDate submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
