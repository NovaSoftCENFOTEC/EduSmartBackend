package com.project.demo.rest.grade.dto;

import com.project.demo.logic.entity.grade.Grade;

/**
 * DTO que representa una calificación y sus datos asociados.
 */
public class GradeDTO {

    private Long id;
    private Double grade;
    private String justification;
    private String gradedAt;
    private Long submissionId;
    private Long teacherId;

    /**
     * Constructor por defecto.
     */
    public GradeDTO() {
    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param id            identificador de la calificación
     * @param grade         nota asignada
     * @param justification justificación de la calificación
     * @param gradedAt      fecha de calificación
     * @param submissionId  identificador de la entrega
     * @param teacherId     identificador del profesor
     */
    public GradeDTO(Long id, Double grade, String justification, String gradedAt, Long submissionId, Long teacherId) {
        this.id = id;
        this.grade = grade;
        this.justification = justification;
        this.gradedAt = gradedAt;
        this.submissionId = submissionId;
        this.teacherId = teacherId;
    }

    /**
     * Crea un GradeDTO a partir de una entidad Grade.
     *
     * @param grade entidad Grade
     * @return GradeDTO
     */
    public static GradeDTO fromEntity(Grade grade) {
        return new GradeDTO(
                grade.getId(),
                grade.getGrade(),
                grade.getJustification(),
                grade.getGradedAt() != null ? grade.getGradedAt().toString() : null,
                grade.getSubmission() != null ? grade.getSubmission().getId() : null,
                grade.getTeacher() != null ? grade.getTeacher().getId() : null
        );
    }

    /**
     * Obtiene el identificador de la calificación.
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador de la calificación.
     *
     * @param id identificador
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene la nota asignada.
     *
     * @return nota
     */
    public Double getGrade() {
        return grade;
    }

    /**
     * Establece la nota asignada.
     *
     * @param grade nota
     */
    public void setGrade(Double grade) {
        this.grade = grade;
    }

    /**
     * Obtiene la justificación de la calificación.
     *
     * @return justificación
     */
    public String getJustification() {
        return justification;
    }

    /**
     * Establece la justificación de la calificación.
     *
     * @param justification justificación
     */
    public void setJustification(String justification) {
        this.justification = justification;
    }

    /**
     * Obtiene la fecha de calificación.
     *
     * @return fecha de calificación
     */
    public String getGradedAt() {
        return gradedAt;
    }

    /**
     * Establece la fecha de calificación.
     *
     * @param gradedAt fecha de calificación
     */
    public void setGradedAt(String gradedAt) {
        this.gradedAt = gradedAt;
    }

    /**
     * Obtiene el identificador de la entrega.
     *
     * @return id de la entrega
     */
    public Long getSubmissionId() {
        return submissionId;
    }

    /**
     * Establece el identificador de la entrega.
     *
     * @param submissionId id de la entrega
     */
    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    /**
     * Obtiene el identificador del profesor.
     *
     * @return id del profesor
     */
    public Long getTeacherId() {
        return teacherId;
    }

    /**
     * Establece el identificador del profesor.
     *
     * @param teacherId id del profesor
     */
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}
