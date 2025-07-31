package com.project.demo.logic.entity.taskSubmission;

import com.project.demo.logic.entity.taskSubmission.TaskSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskSubmissionRepository extends JpaRepository<TaskSubmission, Long> {

    Page<TaskSubmission> findByStudentId(Long studentId, Pageable pageable);

    Page<TaskSubmission> findByAssignmentId(Long assignmentId, Pageable pageable);
}
