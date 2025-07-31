package com.project.demo.logic.entity.taskSubmission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface taskSubmissionRepository<taskSubmission> extends JpaRepository<taskSubmission, Long>{

    Page<taskSubmission> findByStudentId(Long studentId, Pageable pageable);

    Page<taskSubmission> findByAssignmentId(Long assignmentId, Pageable pageable);


}

