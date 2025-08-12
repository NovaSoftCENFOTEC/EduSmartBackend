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

/**
 * Controlador REST para la gestión de preguntas de cuestionarios.
 * Permite crear, consultar, actualizar y eliminar preguntas asociadas a quizzes.
 */
@RestController
@RequestMapping("/questions")
public class QuestionRestController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    /**
     * Obtiene las preguntas asociadas a un quiz.
     * @param quizId identificador del quiz
     * @param request petición HTTP
     * @return lista de preguntas
     */
    @GetMapping("/quiz/{quizId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getQuestionsByQuiz(@PathVariable Integer quizId, HttpServletRequest request) {
        List<Question> questions = questionRepository.findByQuizId(quizId);
        return new GlobalResponseHandler().handleResponse("Preguntas obtenidas correctamente", questions, HttpStatus.OK, request);
    }

    /**
     * Obtiene una pregunta por su identificador.
     * @param id identificador de la pregunta
     * @param request petición HTTP
     * @return pregunta encontrada
     */
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

    /**
     * Obtiene las preguntas asociadas a un quiz para estudiantes.
     * @param quizId identificador del quiz
     * @param request petición HTTP
     * @return lista de preguntas
     */
    @GetMapping("/quiz/{quizId}/student")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getQuestionsByQuizForStudent(@PathVariable Integer quizId, HttpServletRequest request) {
        List<Question> questions = questionRepository.findByQuizId(quizId);
        return new GlobalResponseHandler().handleResponse("Preguntas obtenidas correctamente", questions, HttpStatus.OK, request);
    }

    /**
     * Crea una nueva pregunta para un quiz.
     * @param quizId identificador del quiz
     * @param question datos de la pregunta
     * @param request petición HTTP
     * @return pregunta creada
     */
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

    /**
     * Actualiza los datos de una pregunta existente.
     * @param id identificador de la pregunta
     * @param questionDetails datos actualizados
     * @param request petición HTTP
     * @return pregunta actualizada
     */
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

    /**
     * Elimina una pregunta por su identificador.
     * @param id identificador de la pregunta
     * @param request petición HTTP
     * @return resultado de la eliminación
     */
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