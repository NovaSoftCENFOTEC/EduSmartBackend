package com.project.demo.logic.entity.quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    List<Quiz> findByStory_Course_TeacherId(Integer teacherId);

    List<Quiz> findByStory_Course_Groups_Id(Integer groupId);

    List<Quiz> findByStoryId(Integer storyId);

    List<Quiz> findByDueDateAfter(java.util.Date now);

    List<Quiz> findByTitleContainingIgnoreCase(String title);
}