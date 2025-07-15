package com.project.demo.logic.entity.answer;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findBySubmissionId(Integer submissionId);

    Optional<Answer> findBySubmissionIdAndQuestionId(Integer submissionId, Integer questionId);

    List<Answer> findByQuestionId(Integer questionId);
}