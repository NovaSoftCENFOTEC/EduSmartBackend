package com.project.demo.logic.entity.grade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Page<Grade> findBySubmissionId(Long submissionId, Pageable pageable);

    Page<Grade> findBySubmissionStudentId(Long studentId, Pageable pageable);
}
