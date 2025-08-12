package com.project.demo.rest.option;

import com.project.demo.logic.entity.option.Option;
import com.project.demo.logic.entity.option.OptionRepository;
import com.project.demo.logic.entity.question.Question;
import com.project.demo.logic.entity.question.QuestionRepository;
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
 * Controlador REST para la gestión de opciones de preguntas.
 * Permite crear, consultar, actualizar y eliminar opciones asociadas a preguntas.
 */
@RestController
@RequestMapping("/options")
public class OptionRestController {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Obtiene las opciones asociadas a una pregunta.
     * @param questionId identificador de la pregunta
     * @param request petición HTTP
     * @return lista de opciones
     */
    @GetMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getOptionsByQuestion(@PathVariable Integer questionId, HttpServletRequest request) {
        List<Option> options = optionRepository.findByQuestionId(questionId);
        return new GlobalResponseHandler().handleResponse("Opciones obtenidas correctamente", options, HttpStatus.OK, request);
    }

    /**
     * Obtiene una opción por su identificador.
     * @param id identificador de la opción
     * @param request petición HTTP
     * @return opción encontrada
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getOptionById(@PathVariable Integer id, HttpServletRequest request) {
        Optional<Option> option = optionRepository.findById(id);
        if (option.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Opción obtenida correctamente", option.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Opción " + id + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Obtiene las opciones asociadas a una pregunta para estudiantes.
     * @param questionId identificador de la pregunta
     * @param request petición HTTP
     * @return lista de opciones
     */
    @GetMapping("/question/{questionId}/student")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getOptionsByQuestionForStudent(@PathVariable Integer questionId, HttpServletRequest request) {
        List<Option> options = optionRepository.findByQuestionId(questionId);
        return new GlobalResponseHandler().handleResponse("Opciones obtenidas correctamente", options, HttpStatus.OK, request);
    }

    /**
     * Crea una nueva opción para una pregunta.
     * @param questionId identificador de la pregunta
     * @param option datos de la opción
     * @param request petición HTTP
     * @return opción creada
     */
    @PostMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> createOption(@PathVariable Integer questionId, @RequestBody Option option, HttpServletRequest request) {
        Optional<Question> foundQuestion = questionRepository.findById(questionId);
        if (foundQuestion.isPresent()) {
            option.setQuestion(foundQuestion.get());
            optionRepository.save(option);
            return new GlobalResponseHandler().handleResponse("Opción creada con éxito", option, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Pregunta " + questionId + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Actualiza los datos de una opción existente.
     * @param id identificador de la opción
     * @param optionDetails datos actualizados
     * @param request petición HTTP
     * @return opción actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateOption(@PathVariable Integer id, @RequestBody Option optionDetails, HttpServletRequest request) {
        Optional<Option> foundOption = optionRepository.findById(id);
        if (foundOption.isPresent()) {
            Option option = foundOption.get();
            option.setText(optionDetails.getText());
            option.setCorrect(optionDetails.isCorrect());
            optionRepository.save(option);
            return new GlobalResponseHandler().handleResponse("Opción actualizada con éxito", option, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Opción " + id + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Elimina una opción por su identificador.
     * @param id identificador de la opción
     * @param request petición HTTP
     * @return resultado de la eliminación
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteOption(@PathVariable Integer id, HttpServletRequest request) {
        Optional<Option> foundOption = optionRepository.findById(id);
        if (foundOption.isPresent()) {
            optionRepository.delete(foundOption.get());
            return new GlobalResponseHandler().handleResponse("Opción eliminada con éxito", foundOption.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Opción " + id + " no encontrada", HttpStatus.NOT_FOUND, request);
        }
    }
}