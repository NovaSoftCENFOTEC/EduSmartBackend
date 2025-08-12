package com.project.demo.rest.grade;

import com.project.demo.logic.entity.grade.Grade;
import com.project.demo.logic.entity.grade.GradeRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.taskSubmission.TaskSubmission;
import com.project.demo.logic.entity.taskSubmission.TaskSubmissionRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.rest.grade.dto.GradeDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de calificaciones.
 * Permite crear, consultar, actualizar y eliminar calificaciones de entregas de tareas.
 */
@RestController
@RequestMapping("/grades")
public class GradeRestController {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private TaskSubmissionRepository taskSubmissionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Obtiene todas las calificaciones paginadas.
     *
     * @param page    número de página
     * @param size    tamaño de página
     * @param request petición HTTP
     * @return lista de calificaciones
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> getAllGrades(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Grade> grades = gradeRepository.findAll(pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(grades.getTotalPages());
        meta.setTotalElements(grades.getTotalElements());
        meta.setPageNumber(grades.getNumber() + 1);
        meta.setPageSize(grades.getSize());

        var gradeDtos = grades.getContent()
                .stream()
                .map(GradeDTO::fromEntity)
                .collect(Collectors.toList());

        return new GlobalResponseHandler().handleResponse("Calificaciones obtenidas con éxito",
                gradeDtos, HttpStatus.OK, meta);
    }

    /**
     * Obtiene una calificación por su identificador.
     *
     * @param gradeId identificador de la calificación
     * @param request petición HTTP
     * @return calificación encontrada
     */
    @GetMapping("/{gradeId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> getGradeById(@PathVariable Long gradeId, HttpServletRequest request) {
        Optional<Grade> found = gradeRepository.findById(gradeId);
        if (found.isPresent()) {
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            GradeDTO dto = GradeDTO.fromEntity(found.get());
            return new GlobalResponseHandler().handleResponse("Calificación obtenida con éxito",
                    dto, HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Calificación " + gradeId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Obtiene las calificaciones por el identificador de la entrega.
     *
     * @param submissionId identificador de la entrega
     * @param page         número de página
     * @param size         tamaño de página
     * @param request      petición HTTP
     * @return lista de calificaciones de la entrega
     */
    @GetMapping("/submission/{submissionId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> getBySubmissionId(@PathVariable Long submissionId,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Grade> grades = gradeRepository.findBySubmissionId(submissionId, pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(grades.getTotalPages());
        meta.setTotalElements(grades.getTotalElements());
        meta.setPageNumber(grades.getNumber() + 1);
        meta.setPageSize(grades.getSize());

        var gradeDtos = grades.getContent()
                .stream()
                .map(GradeDTO::fromEntity)
                .collect(Collectors.toList());

        return new GlobalResponseHandler().handleResponse("Calificaciones por entrega obtenidas con éxito",
                gradeDtos, HttpStatus.OK, meta);
    }

    /**
     * Crea una nueva calificación para una entrega de tarea.
     *
     * @param grade   datos de la calificación
     * @param request petición HTTP
     * @return calificación creada
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> createGrade(@RequestBody Grade grade, HttpServletRequest request) {

        if (grade.getSubmission() == null || grade.getSubmission().getId() == null) {
            return new GlobalResponseHandler().handleResponse("El ID de la entrega es obligatorio",
                    HttpStatus.BAD_REQUEST, request);
        }

        if (grade.getTeacher() == null || grade.getTeacher().getId() == null) {
            return new GlobalResponseHandler().handleResponse("El ID del profesor es obligatorio",
                    HttpStatus.BAD_REQUEST, request);
        }

        Optional<TaskSubmission> submission = taskSubmissionRepository.findById(grade.getSubmission().getId());
        Optional<User> teacher = userRepository.findById(grade.getTeacher().getId());

        if (submission.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Entrega de tarea no encontrada",
                    HttpStatus.BAD_REQUEST, request);
        }

        if (teacher.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Profesor no encontrado",
                    HttpStatus.BAD_REQUEST, request);
        }

        grade.setSubmission(submission.get());
        grade.setTeacher(teacher.get());

        Grade saved = gradeRepository.save(grade);
        GradeDTO dto = GradeDTO.fromEntity(saved);

        return new GlobalResponseHandler().handleResponse("Calificación registrada con éxito",
                dto, HttpStatus.CREATED, request);
    }

    /**
     * Actualiza una calificación existente.
     *
     * @param gradeId  identificador de la calificación
     * @param gradeDto datos actualizados
     * @param request  petición HTTP
     * @return calificación actualizada
     */
    @PutMapping("/{gradeId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> updateGrade(@PathVariable Long gradeId,
                                         @RequestBody GradeDTO gradeDto,
                                         HttpServletRequest request) {

        Optional<Grade> found = gradeRepository.findById(gradeId);
        if (found.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Calificación no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }

        Grade updated = found.get();
        updated.setGrade(gradeDto.getGrade());
        updated.setJustification(gradeDto.getJustification());

        gradeRepository.save(updated);

        GradeDTO dto = GradeDTO.fromEntity(updated);

        return new GlobalResponseHandler().handleResponse("Calificación actualizada con éxito",
                dto, HttpStatus.OK, request);
    }

    /**
     * Elimina una calificación por su identificador.
     *
     * @param gradeId identificador de la calificación
     * @param request petición HTTP
     * @return resultado de la eliminación
     */
    @DeleteMapping("/{gradeId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> deleteGrade(@PathVariable Long gradeId, HttpServletRequest request) {
        Optional<Grade> found = gradeRepository.findById(gradeId);
        if (found.isPresent()) {
            gradeRepository.delete(found.get());
            return new GlobalResponseHandler().handleResponse("Calificación eliminada con éxito",
                    null, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Calificación no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Obtiene las calificaciones de un estudiante por su identificador.
     *
     * @param studentId identificador del estudiante
     * @param page      número de página
     * @param size      tamaño de página
     * @param request   petición HTTP
     * @return lista de calificaciones del estudiante
     */
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> getGradesByStudentId(@PathVariable Long studentId,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Grade> grades = gradeRepository.findBySubmissionStudentId(studentId, pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(grades.getTotalPages());
        meta.setTotalElements(grades.getTotalElements());
        meta.setPageNumber(grades.getNumber() + 1);
        meta.setPageSize(grades.getSize());

        var gradeDtos = grades.getContent()
                .stream()
                .map(GradeDTO::fromEntity)
                .collect(Collectors.toList());

        return new GlobalResponseHandler().handleResponse("Calificaciones del estudiante obtenidas con éxito",
                gradeDtos, HttpStatus.OK, meta);
    }
}
