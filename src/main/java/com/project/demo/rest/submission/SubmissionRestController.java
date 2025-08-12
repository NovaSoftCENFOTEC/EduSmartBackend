package com.project.demo.rest.submission;
import com.project.demo.logic.entity.submission.Submission;
import com.project.demo.logic.entity.submission.SubmissionRepository;
import com.project.demo.logic.entity.quiz.Quiz;
import com.project.demo.logic.entity.quiz.QuizRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de entregas de cuestionarios.
 * Permite crear, consultar, actualizar y eliminar entregas asociadas a quizzes y estudiantes.
 */
@RestController
@RequestMapping("/submissions")
public class SubmissionRestController {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Obtiene las entregas asociadas a un quiz.
     * @param quizId identificador del quiz
     * @param request petición HTTP
     * @return lista de entregas
     */
    @GetMapping("/quiz/{quizId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getSubmissionsByQuiz(@PathVariable Integer quizId, HttpServletRequest request) {
        List<Submission> submissions = submissionRepository.findByQuizId(quizId);
        return new GlobalResponseHandler().handleResponse("Submissions obtenidas correctamente", submissions, HttpStatus.OK, request);
    }

    /**
     * Obtiene una entrega por su identificador.
     * @param id identificador de la entrega
     * @param request petición HTTP
     * @return entrega encontrada
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getSubmissionById(@PathVariable Integer id, HttpServletRequest request) {
        Optional<Submission> submission = submissionRepository.findById(id);
        if (submission.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Submission obtenida correctamente", submission.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Submission " + id + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Crea una nueva entrega para un quiz y estudiante.
     * @param quizId identificador del quiz
     * @param studentId identificador del estudiante
     * @param submission datos de la entrega (opcional)
     * @param request petición HTTP
     * @return entrega creada
     */
    @PostMapping("/quiz/{quizId}/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> createSubmission(@PathVariable Integer quizId, @PathVariable Integer studentId,
                                              @RequestBody(required = false) Submission submission, HttpServletRequest request) {
        Optional<Quiz> foundQuiz = quizRepository.findById(quizId);
        if (!foundQuiz.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Quiz " + quizId + " no encontrado", HttpStatus.NOT_FOUND, request);
        }

        Optional<User> foundStudent = userRepository.findById(Long.valueOf(studentId));
        if (!foundStudent.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Estudiante " + studentId + " no encontrado", HttpStatus.NOT_FOUND, request);
        }

        Optional<Submission> existingSubmission = submissionRepository.findByQuizIdAndStudentId(quizId, studentId);
        if (existingSubmission.isPresent()) {
            return new GlobalResponseHandler().handleResponse("El estudiante ya ha realizado una submission para este quiz", HttpStatus.CONFLICT, request);
        }

        Quiz quiz = foundQuiz.get();
        if (quiz.getDueDate() != null && quiz.getDueDate().isBefore(LocalDateTime.now())) {
            return new GlobalResponseHandler().handleResponse("El quiz ya ha vencido", HttpStatus.BAD_REQUEST, request);
        }

        Submission newSubmission = new Submission();
        newSubmission.setQuiz(foundQuiz.get());
        newSubmission.setStudent(foundStudent.get());
        newSubmission.setSubmittedAt(LocalDateTime.now());
        newSubmission.setScore(0.0);
        submissionRepository.save(newSubmission);
        return new GlobalResponseHandler().handleResponse("Submission creada con éxito", newSubmission, HttpStatus.OK, request);
    }

    /**
     * Actualiza los datos de una entrega existente.
     * @param id identificador de la entrega
     * @param submissionDetails datos actualizados
     * @param request petición HTTP
     * @return entrega actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateSubmission(@PathVariable Integer id, @RequestBody Submission submissionDetails, HttpServletRequest request) {
        Optional<Submission> foundSubmission = submissionRepository.findById(id);
        if (foundSubmission.isPresent()) {
            Submission submission = foundSubmission.get();
            submission.setScore(submissionDetails.getScore());
            submissionRepository.save(submission);
            return new GlobalResponseHandler().handleResponse("Submission actualizada con éxito", submission, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Submission " + id + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Elimina una entrega por su identificador.
     * @param id identificador de la entrega
     * @param request petición HTTP
     * @return resultado de la eliminación
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteSubmission(@PathVariable Integer id, HttpServletRequest request) {
        Optional<Submission> foundSubmission = submissionRepository.findById(id);
        if (foundSubmission.isPresent()) {
            submissionRepository.delete(foundSubmission.get());
            return new GlobalResponseHandler().handleResponse("Submission eliminada con éxito", foundSubmission.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Submission " + id + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Obtiene las entregas asociadas a un estudiante.
     * @param studentId identificador del estudiante
     * @param request petición HTTP
     * @return lista de entregas del estudiante
     */
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getSubmissionsByStudent(@PathVariable Integer studentId, HttpServletRequest request) {
        List<Submission> submissions = submissionRepository.findByStudentId(studentId);
        return new GlobalResponseHandler().handleResponse("Submissions del estudiante obtenidas correctamente", submissions, HttpStatus.OK, request);
    }
}