package com.project.demo.logic.entity.quiz;

import com.project.demo.logic.entity.story.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private StoryRepository storyRepository;

    public List<Quiz> getAllQuizzes(){
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(Integer id) {
        return quizRepository.findById(id);
    }

    public Optional<Quiz> createQuiz(Integer storyId, Quiz quiz) {
        return storyRepository.findById(Long.valueOf(storyId)).map(story -> {
            quiz.setStory(story);
            return quizRepository.save(quiz);
        });
    }

    public Optional<Quiz> updateQuiz(Integer id, Quiz quizDetails) {
        return quizRepository.findById(id).map(quiz -> {
            quiz.setTitle(quizDetails.getTitle());
            quiz.setDescription(quizDetails.getDescription());
            quiz.setDueDate(quizDetails.getDueDate());
            return quizRepository.save(quiz);
        });
    }

    public Optional<Quiz> deleteQuiz(Integer id) {
        return quizRepository.findById(id).map(quiz -> {
            quizRepository.delete(quiz);
            return quiz;
        });
    }
}
