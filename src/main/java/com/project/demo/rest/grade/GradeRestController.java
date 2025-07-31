package com.project.demo.rest.grade;

import com.project.demo.logic.entity.grade.Grade;
import com.project.demo.logic.entity.grade.GradeRepository;
import com.project.demo.logic.entity.taskSubmission.TaskSubmission;
import com.project.demo.logic.entity.taskSubmission.TaskSubmissionRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/grades")
public class GradeRestController {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private TaskSubmissionRepository taskSubmissionRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
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

        return new GlobalResponseHandler().handleResponse("Calificaciones obtenidas con éxito",
                grades.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/{gradeId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> getGradeById(@PathVariable Long gradeId, HttpServletRequest request) {
        Optional<Grade> found = gradeRepository.findById(gradeId);
        if (found.isPresent()) {
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            return new GlobalResponseHandler().handleResponse("Calificación obtenida con éxito",
                    found.get(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Calificación " + gradeId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/submission/{submissionId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
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

        return new GlobalResponseHandler().handleResponse("Calificaciones por entrega obtenidas con éxito",
                grades.getContent(), HttpStatus.OK, meta);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> createGrade(@RequestBody Grade grade, HttpServletRequest request) {

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

        return new GlobalResponseHandler().handleResponse("Calificación registrada con éxito",
                saved, HttpStatus.CREATED, request);
    }

    @PutMapping("/{gradeId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateGrade(@PathVariable Long gradeId,
                                         @RequestBody Grade grade,
                                         HttpServletRequest request) {

        Optional<Grade> found = gradeRepository.findById(gradeId);
        if (found.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Calificación no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }

        Grade updated = found.get();
        updated.setGrade(grade.getGrade());
        updated.setJustification(grade.getJustification());

        gradeRepository.save(updated);

        return new GlobalResponseHandler().handleResponse("Calificación actualizada con éxito",
                updated, HttpStatus.OK, request);
    }

    @DeleteMapping("/{gradeId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
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

}
