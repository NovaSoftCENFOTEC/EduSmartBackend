package com.project.demo.rest.taskSubmission.dto;

import com.project.demo.logic.entity.taskSubmission.TaskSubmission;
import java.time.LocalDate;

/**
 * DTO que representa una entrega de tarea y sus datos asociados.
 */
public class TaskSubmissionDTO {

    private Long id;
    private String fileUrl;
    private String comment;
    private LocalDate submittedAt;
    private Long assignmentId;
    private Long studentId;

    /**
     * Constructor por defecto.
     */
    public TaskSubmissionDTO() {}

    /**
     * Constructor con todos los parámetros.
     * @param fileUrl URL del archivo entregado
     * @param comment comentario del estudiante
     * @param submittedAt fecha de entrega
     * @param assignmentId identificador de la asignación
     * @param studentId identificador del estudiante
     */
    public TaskSubmissionDTO(String fileUrl, String comment, LocalDate submittedAt, Long assignmentId, Long studentId) {
        this.fileUrl = fileUrl;
        this.comment = comment;
        this.submittedAt = submittedAt;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
    }

    /**
     * Crea un TaskSubmissionDTO a partir de una entidad TaskSubmission.
     * @param submission entidad TaskSubmission
     * @return TaskSubmissionDTO
     */
    public static TaskSubmissionDTO fromEntity(TaskSubmission submission) {
        TaskSubmissionDTO dto = new TaskSubmissionDTO(
                submission.getFileUrl(),
                submission.getComment(),
                submission.getSubmittedAt(),
                submission.getAssignment() != null ? submission.getAssignment().getId() : null,
                submission.getStudent() != null ? submission.getStudent().getId() : null
        );
        dto.setId(submission.getId());
        return dto;
    }

    /**
     * Obtiene el identificador de la entrega.
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador de la entrega.
     * @param id identificador
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
     * Obtiene la fecha de entrega.
     * @return fecha de entrega
     */
    public LocalDate getSubmittedAt() {
        return submittedAt;
    }

    /**
     * Establece la fecha de entrega.
     * @param submittedAt fecha de entrega
     */
    public void setSubmittedAt(LocalDate submittedAt) {
        this.submittedAt = submittedAt;
    }

    /**
     * Obtiene el identificador de la asignación.
     * @return id de la asignación
     */
    public Long getAssignmentId() {
        return assignmentId;
    }

    /**
     * Establece el identificador de la asignación.
     * @param assignmentId id de la asignación
     */
    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    /**
     * Obtiene el identificador del estudiante.
     * @return id del estudiante
     */
    public Long getStudentId() {
        return studentId;
    }

    /**
     * Establece el identificador del estudiante.
     * @param studentId id del estudiante
     */
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
