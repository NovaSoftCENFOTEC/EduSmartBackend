package com.project.demo.logic.entity.submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

    Optional<Submission> findByQuizIdAndStudentId(Integer quizId, Integer studentId);

    List<Submission> findByQuizId(Integer quizId);

    List<Submission> findByStudentId(Integer studentId);
}