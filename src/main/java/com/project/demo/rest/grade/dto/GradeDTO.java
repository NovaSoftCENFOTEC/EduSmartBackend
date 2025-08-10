package com.project.demo.rest.grade.dto;

import com.project.demo.logic.entity.grade.Grade;

public class GradeDTO {

    private Long id;
    private Double grade;
    private String justification;
    private String gradedAt;
    private Long submissionId;
    private Long teacherId;

    public GradeDTO() {}

    public GradeDTO(Long id, Double grade, String justification, String gradedAt, Long submissionId, Long teacherId) {
        this.id = id;
        this.grade = grade;
        this.justification = justification;
        this.gradedAt = gradedAt;
        this.submissionId = submissionId;
        this.teacherId = teacherId;
    }

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }

    public String getGradedAt() { return gradedAt; }
    public void setGradedAt(String gradedAt) { this.gradedAt = gradedAt; }

    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
}

