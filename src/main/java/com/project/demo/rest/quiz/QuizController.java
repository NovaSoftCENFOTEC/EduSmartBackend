package com.project.demo.rest.quiz;


import com.project.demo.logic.entity.question.QuestionGenerationService;
import com.project.demo.logic.entity.quiz.Quiz;
import com.project.demo.logic.entity.quiz.QuizService;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionGenerationService questionGenerationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAllQuizzes(HttpServletRequest request) {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        return new GlobalResponseHandler().handleResponse("Quizzes obtenidos correctamente", quizzes, HttpStatus.OK,
                request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getQuizById(@PathVariable Integer id, HttpServletRequest request) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Quiz obtenido correctamente", quiz.get(), HttpStatus.OK,
                    request);
        } else {
            return new GlobalResponseHandler().handleResponse("Quiz " + id + " no encontrado", HttpStatus.NOT_FOUND,
                    request);
        }
    }

    @PostMapping("/story/{storyId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> createQuiz(@PathVariable Integer storyId, @RequestBody Quiz quiz, HttpServletRequest request) {
        try {
            Optional<Quiz> savedQuiz = quizService.createQuiz(storyId, quiz);
            if (savedQuiz.isPresent()) {
                if (quiz.isGenerateWithAI() && quiz.getNumberOfQuestions() > 0) {
                    questionGenerationService.generateQuestionsWithAI(savedQuiz.get(), quiz.getNumberOfQuestions());
                }
                return new GlobalResponseHandler().handleResponse("Quiz creado con éxito", savedQuiz.get(), HttpStatus.OK, request);
            } else {
                return new GlobalResponseHandler().handleResponse("Historia " + storyId + " no encontrada", HttpStatus.NOT_FOUND, request);
            }
        } catch (Exception e) {
            return new GlobalResponseHandler().handleResponse("Error al crear el quiz: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }

    @PostMapping("/{quizId}/generate-questions")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> generateQuestionsForQuiz(@PathVariable Integer quizId, @RequestParam int numberOfQuestions, HttpServletRequest request) {
        Optional<Quiz> quizOpt = quizService.getQuizById(quizId);
        if (!quizOpt.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Quiz no encontrado", HttpStatus.NOT_FOUND, request);
        }
        questionGenerationService.generateQuestionsWithAI(quizOpt.get(), numberOfQuestions);
        return new GlobalResponseHandler().handleResponse("Preguntas generadas con éxito", HttpStatus.OK, request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateQuiz(@PathVariable Integer id, @RequestBody Quiz quizDetails, HttpServletRequest request) {
        Optional<Quiz> updatedQuiz = quizService.updateQuiz(id, quizDetails);
        if (updatedQuiz.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Quiz actualizado con éxito", updatedQuiz.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Quiz " + id + " no encontrado", HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteQuiz(@PathVariable Integer id, HttpServletRequest request) {
        Optional<Quiz> deletedQuiz = quizService.deleteQuiz(id);
        if (deletedQuiz.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Quiz eliminado con éxito", deletedQuiz.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Quiz " + id + " no encontrado", HttpStatus.NOT_FOUND, request);
        }
    }
}