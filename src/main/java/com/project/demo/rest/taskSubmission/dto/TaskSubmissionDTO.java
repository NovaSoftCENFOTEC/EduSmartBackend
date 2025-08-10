package com.project.demo.rest.taskSubmission.dto;

import com.project.demo.logic.entity.taskSubmission.TaskSubmission;
import java.time.LocalDate;

public class TaskSubmissionDTO {

    private Long id;
    private String fileUrl;
    private String comment;
    private LocalDate submittedAt;
    private Long assignmentId;
    private Long studentId;

    public TaskSubmissionDTO() {}

    public TaskSubmissionDTO(String fileUrl, String comment, LocalDate submittedAt, Long assignmentId, Long studentId) {
        this.fileUrl = fileUrl;
        this.comment = comment;
        this.submittedAt = submittedAt;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
    }
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

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }


}
