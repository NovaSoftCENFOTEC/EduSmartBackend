package com.project.demo.logic.entity.taskSubmission;
import com.project.demo.logic.entity.assignment.Assignment;
import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

/**
 * Representa una entrega de tarea realizada por un estudiante.
 * Incluye información como archivo, comentario, fecha de entrega, tarea y estudiante.
 */
@Table(name = "task_submission")
@Entity
public class TaskSubmission {
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

    /**
     * Constructor por defecto.
     */
    public TaskSubmission() {
    }

    /**
     * Constructor con todos los parámetros.
     * @param id identificador de la entrega
     * @param fileUrl URL del archivo entregado
     * @param comment comentario del estudiante
     * @param submittedAt fecha de entrega
     * @param assignment tarea asociada
     * @param student estudiante que entrega
     */
    public TaskSubmission(Long id, String fileUrl, String comment, LocalDate submittedAt, Assignment assignment, User student) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.comment = comment;
        this.submittedAt = submittedAt;
        this.assignment = assignment;
        this.student = student;
    }

    /**
     * Obtiene el identificador único de la entrega.
     * @return id de la entrega
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único de la entrega.
     * @param id identificador de la entrega
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene la URL del archivo entregado.
     * @return URL del archivo
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * Establece la URL del archivo entregado.
     * @param fileUrl URL del archivo
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * Obtiene el comentario del estudiante.
     * @return comentario
     */
    public String getComment() {
        return comment;
    }

    /**
     * Establece el comentario del estudiante.
     * @param comment comentario
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Obtiene la fecha en que se realizó la entrega.
     * @return fecha de entrega
     */
    public LocalDate getSubmittedAt() {
        return submittedAt;
    }

    /**
     * Establece la fecha en que se realizó la entrega.
     * @param submittedAt fecha de entrega
     */
    public void setSubmittedAt(LocalDate submittedAt) {
        this.submittedAt = submittedAt;
    }

    /**
     * Obtiene la tarea asociada a la entrega.
     * @return tarea
     */
    public Assignment getAssignment() {
        return assignment;
    }

    /**
     * Establece la tarea asociada a la entrega.
     * @param assignment tarea
     */
    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    /**
     * Obtiene el estudiante que realizó la entrega.
     * @return estudiante
     */
    public User getStudent() {
        return student;
    }

    /**
     * Establece el estudiante que realizó la entrega.
     * @param student estudiante
     */
    public void setStudent(User student) {
        this.student = student;
    }
}
