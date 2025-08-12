package com.project.demo.logic.entity.grade;

import com.project.demo.logic.entity.taskSubmission.TaskSubmission;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Representa una calificación asignada a una entrega de tarea.
 * Incluye la nota, justificación, fecha de calificación, entrega y profesor.
 */
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

    /**
     * Constructor por defecto.
     */
    public Grade() {}

    /**
     * Constructor con parámetros.
     * @param grade nota asignada
     * @param justification justificación de la calificación
     * @param submission entrega de la tarea
     * @param teacher profesor que califica
     */
    public Grade(Double grade, String justification, TaskSubmission submission, User teacher) {
        this.grade = grade;
        this.justification = justification;
        this.submission = submission;
        this.teacher = teacher;
    }

    /**
     * Obtiene el identificador único de la calificación.
     * @return id de la calificación
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la calificación.
     * @param id identificador de la calificación
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene la nota asignada.
     * @return nota
     */
    public Double getGrade() {
        return grade;
    }

    /**
     * Establece la nota asignada.
     * @param grade nota
     */
    public void setGrade(Double grade) {
        this.grade = grade;
    }

    /**
     * Obtiene la justificación de la calificación.
     * @return justificación
     */
    public String getJustification() {
        return justification;
    }

    /**
     * Establece la justificación de la calificación.
     * @param justification justificación
     */
    public void setJustification(String justification) {
        this.justification = justification;
    }

    /**
     * Obtiene la fecha en que se realizó la calificación.
     * @return fecha de calificación
     */
    public LocalDateTime getGradedAt() {
        return gradedAt;
    }

    /**
     * Establece la fecha en que se realizó la calificación.
     * @param gradedAt fecha de calificación
     */
    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }

    /**
     * Obtiene la entrega de tarea asociada.
     * @return entrega de tarea
     */
    public TaskSubmission getSubmission() {
        return submission;
    }

    /**
     * Establece la entrega de tarea asociada.
     * @param submission entrega de tarea
     */
    public void setSubmission(TaskSubmission submission) {
        this.submission = submission;
    }

    /**
     * Obtiene el profesor que realizó la calificación.
     * @return profesor
     */
    public User getTeacher() {
        return teacher;
    }

    /**
     * Establece el profesor que realizó la calificación.
     * @param teacher profesor
     */
    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}
