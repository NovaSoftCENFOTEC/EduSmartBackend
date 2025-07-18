package com.project.demo.logic.entity.question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByQuizId(Integer quizId);

    List<Question> findByTextContainingIgnoreCase(String text);
}