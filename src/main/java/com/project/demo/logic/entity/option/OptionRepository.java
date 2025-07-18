package com.project.demo.logic.entity.option;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    List<Option> findByQuestionId(Integer questionId);

    Option findByQuestionIdAndIsCorrectTrue(Integer questionId);
}