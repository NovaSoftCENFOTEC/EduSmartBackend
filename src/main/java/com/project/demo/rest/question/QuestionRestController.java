package com.project.demo.rest.question;
import com.project.demo.logic.entity.question.Question;
import com.project.demo.logic.entity.question.QuestionRepository;
import com.project.demo.logic.entity.quiz.Quiz;
import com.project.demo.logic.entity.quiz.QuizRepository;
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
@RequestMapping("/questions")
public class QuestionRestController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @GetMapping("/quiz/{quizId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getQuestionsByQuiz(@PathVariable Integer quizId, HttpServletRequest request) {
        List<Question> questions = questionRepository.findByQuizId(quizId);
        return new GlobalResponseHandler().handleResponse("Preguntas obtenidas correctamente", questions, HttpStatus.OK, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getQuestionById(@PathVariable Integer id, HttpServletRequest request) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Pregunta obtenida correctamente", question.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Pregunta " + id + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/quiz/{quizId}/student")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getQuestionsByQuizForStudent(@PathVariable Integer quizId, HttpServletRequest request) {
        List<Question> questions = questionRepository.findByQuizId(quizId);
        return new GlobalResponseHandler().handleResponse("Preguntas obtenidas correctamente", questions, HttpStatus.OK, request);
    }

    @PostMapping("/quiz/{quizId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> createQuestion(@PathVariable Integer quizId, @RequestBody Question question, HttpServletRequest request) {
        Optional<Quiz> foundQuiz = quizRepository.findById(quizId);
        if (foundQuiz.isPresent()) {
            question.setQuiz(foundQuiz.get());
            questionRepository.save(question);
            return new GlobalResponseHandler().handleResponse("Pregunta creada con éxito", question, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Quiz " + quizId + " no encontrado", HttpStatus.NOT_FOUND, request);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateQuestion(@PathVariable Integer id, @RequestBody Question questionDetails, HttpServletRequest request) {
        Optional<Question> foundQuestion = questionRepository.findById(id);
        if (foundQuestion.isPresent()) {
            Question question = foundQuestion.get();
            question.setText(questionDetails.getText());
            questionRepository.save(question);
            return new GlobalResponseHandler().handleResponse("Pregunta actualizada con éxito", question, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Pregunta " + id + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteQuestion(@PathVariable Integer id, HttpServletRequest request) {
        Optional<Question> foundQuestion = questionRepository.findById(id);
        if (foundQuestion.isPresent()) {
            questionRepository.delete(foundQuestion.get());
            return new GlobalResponseHandler().handleResponse("Pregunta eliminada con éxito", foundQuestion.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Pregunta " + id + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }
}