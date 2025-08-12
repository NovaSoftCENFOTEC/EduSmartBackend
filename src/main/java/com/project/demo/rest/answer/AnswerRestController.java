package com.project.demo.rest.answer;

import com.project.demo.logic.entity.answer.Answer;
import com.project.demo.logic.entity.answer.AnswerRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.option.Option;
import com.project.demo.logic.entity.option.OptionRepository;
import com.project.demo.logic.entity.question.Question;
import com.project.demo.logic.entity.question.QuestionRepository;
import com.project.demo.logic.entity.submission.Submission;
import com.project.demo.logic.entity.submission.SubmissionRepository;
import com.project.demo.logic.request.AnswerRequest;
import com.project.demo.logic.request.AnswerUpdateRequest;
import com.project.demo.rest.answer.dto.AnswerResultDto;
import com.project.demo.rest.submission.dto.SubmissionResultDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de respuestas de cuestionarios.
 * Permite crear, consultar, actualizar y eliminar respuestas.
 */
@RestController
@RequestMapping("/answers")
public class AnswerRestController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    /**
     * Obtiene todas las respuestas asociadas a una entrega.
     *
     * @param submissionId identificador de la entrega
     * @param request      petición HTTP
     * @return respuestas de la entrega
     */
    @GetMapping("/submission/{submissionId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAnswersBySubmission(@PathVariable Integer submissionId, HttpServletRequest request) {
        List<Answer> answers = answerRepository.findBySubmissionId(submissionId);
        return new GlobalResponseHandler().handleResponse("Respuestas obtenidas correctamente", answers, HttpStatus.OK, request);
    }

    /**
     * Obtiene una respuesta por su identificador.
     *
     * @param id      identificador de la respuesta
     * @param request petición HTTP
     * @return respuesta encontrada
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAnswerById(@PathVariable Long id, HttpServletRequest request) {
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Respuesta obtenida correctamente", answer.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Respuesta " + id + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Crea una nueva respuesta para una pregunta en una entrega.
     *
     * @param submissionId identificador de la entrega
     * @param questionId   identificador de la pregunta
     * @param optionId     identificador de la opción seleccionada
     * @param request      petición HTTP
     * @return respuesta creada
     */
    @PostMapping("/submission/{submissionId}/question/{questionId}/option/{optionId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> createAnswer(@PathVariable Integer submissionId,
                                          @PathVariable Integer questionId,
                                          @PathVariable Integer optionId,
                                          HttpServletRequest request) {
        Optional<Submission> foundSubmission = submissionRepository.findById(submissionId);
        if (!foundSubmission.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Submission " + submissionId + " no encontrada", HttpStatus.NOT_FOUND, request);
        }

        Optional<Question> foundQuestion = questionRepository.findById(questionId);
        if (!foundQuestion.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Pregunta " + questionId + " no encontrada", HttpStatus.NOT_FOUND, request);
        }

        Optional<Option> foundOption = optionRepository.findById(optionId);
        if (!foundOption.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Opción " + optionId + " no encontrada", HttpStatus.NOT_FOUND, request);
        }

        Optional<Answer> existingAnswer = answerRepository.findBySubmissionIdAndQuestionId(submissionId, questionId);
        if (existingAnswer.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Ya existe una respuesta para esta pregunta en esta submission", HttpStatus.CONFLICT, request);
        }

        Answer answer = new Answer();
        answer.setSubmission(foundSubmission.get());
        answer.setQuestion(foundQuestion.get());
        answer.setSelectedOption(foundOption.get());

        answerRepository.save(answer);
        return new GlobalResponseHandler().handleResponse("Respuesta creada con éxito", answer, HttpStatus.OK, request);
    }

    /**
     * Crea respuestas en bloque para una entrega.
     *
     * @param submissionId   identificador de la entrega
     * @param answerRequests lista de respuestas
     * @param request        petición HTTP
     * @return respuestas creadas
     */
    @PostMapping("/submission/{submissionId}/bulk")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> createBulkAnswers(@PathVariable Integer submissionId,
                                               @RequestBody List<AnswerRequest> answerRequests,
                                               HttpServletRequest request) {
        Optional<Submission> foundSubmission = submissionRepository.findById(submissionId);
        if (!foundSubmission.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Submission " + submissionId + " no encontrada", HttpStatus.NOT_FOUND, request);
        }

        List<Answer> createdAnswers = new ArrayList<>();

        for (AnswerRequest answerRequest : answerRequests) {
            Optional<Question> foundQuestion = questionRepository.findById(answerRequest.getQuestionId());
            Optional<Option> foundOption = optionRepository.findById(answerRequest.getOptionId());

            if (foundQuestion.isPresent() && foundOption.isPresent()) {
                Optional<Answer> existingAnswer = answerRepository.findBySubmissionIdAndQuestionId(submissionId, answerRequest.getQuestionId());
                if (!existingAnswer.isPresent()) {
                    Answer answer = new Answer();
                    answer.setSubmission(foundSubmission.get());
                    answer.setQuestion(foundQuestion.get());
                    answer.setSelectedOption(foundOption.get());

                    answerRepository.save(answer);
                    createdAnswers.add(answer);
                }
            }
        }

        return new GlobalResponseHandler().handleResponse("Respuestas creadas con éxito", createdAnswers, HttpStatus.OK, request);
    }

    /**
     * Actualiza la opción seleccionada de una respuesta.
     *
     * @param id            identificador de la respuesta
     * @param updateRequest datos de actualización
     * @param request       petición HTTP
     * @return respuesta actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateAnswer(@PathVariable Long id,
                                          @RequestBody AnswerUpdateRequest updateRequest,
                                          HttpServletRequest request) {
        Optional<Answer> foundAnswer = answerRepository.findById(id);
        if (foundAnswer.isPresent()) {
            Optional<Option> foundOption = optionRepository.findById(updateRequest.getOptionId());
            if (foundOption.isPresent()) {
                Answer answer = foundAnswer.get();
                answer.setSelectedOption(foundOption.get());
                answerRepository.save(answer);
                return new GlobalResponseHandler().handleResponse("Respuesta actualizada con éxito", answer, HttpStatus.OK, request);
            } else {
                return new GlobalResponseHandler().handleResponse("Opción " + updateRequest.getOptionId() + " no encontrada", HttpStatus.NOT_FOUND, request);
            }
        } else {
            return new GlobalResponseHandler().handleResponse("Respuesta " + id + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Elimina una respuesta por su identificador.
     *
     * @param id      identificador de la respuesta
     * @param request petición HTTP
     * @return respuesta eliminada
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long id, HttpServletRequest request) {
        Optional<Answer> foundAnswer = answerRepository.findById(id);
        if (foundAnswer.isPresent()) {
            answerRepository.delete(foundAnswer.get());
            return new GlobalResponseHandler().handleResponse("Respuesta eliminada con éxito", foundAnswer.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Respuesta " + id + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Obtiene todas las respuestas asociadas a una pregunta.
     *
     * @param questionId identificador de la pregunta
     * @param request    petición HTTP
     * @return respuestas de la pregunta
     */
    @GetMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAnswersByQuestion(@PathVariable Integer questionId, HttpServletRequest request) {
        List<Answer> answers = answerRepository.findByQuestionId(questionId);
        return new GlobalResponseHandler().handleResponse("Respuestas de la pregunta obtenidas correctamente", answers, HttpStatus.OK, request);
    }

    /**
     * Obtiene los resultados de una entrega, incluyendo el puntaje y respuestas.
     *
     * @param submissionId identificador de la entrega
     * @param request      petición HTTP
     * @return resultados de la entrega
     */
    @GetMapping("/submission/{submissionId}/results")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getSubmissionResults(@PathVariable Integer submissionId, HttpServletRequest request) {
        Optional<Submission> foundSubmission = submissionRepository.findById(submissionId);
        if (!foundSubmission.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Submission " + submissionId + " no encontrada", HttpStatus.NOT_FOUND, request);
        }

        List<Answer> answers = answerRepository.findBySubmissionId(submissionId);

        int totalQuestions = answers.size();
        int correctAnswers = 0;
        List<AnswerResultDto> results = new ArrayList<>();

        for (Answer answer : answers) {
            boolean isCorrect = answer.getSelectedOption().isCorrect();
            if (isCorrect) {
                correctAnswers++;
            }

            AnswerResultDto answerResult = new AnswerResultDto(
                    answer.getQuestion().getText(),
                    answer.getSelectedOption().getText(),
                    isCorrect
            );
            results.add(answerResult);
        }

        double score = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;

        SubmissionResultDto submissionResult = new SubmissionResultDto(
                totalQuestions,
                correctAnswers,
                score,
                results
        );

        return new GlobalResponseHandler().handleResponse("Resultados obtenidos correctamente", submissionResult, HttpStatus.OK, request);
    }
}
