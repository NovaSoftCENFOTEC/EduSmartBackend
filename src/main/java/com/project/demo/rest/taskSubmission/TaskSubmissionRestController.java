package com.project.demo.rest.taskSubmission;

import com.project.demo.logic.entity.taskSubmission.TaskSubmission;
import com.project.demo.logic.entity.taskSubmission.TaskSubmissionRepository;
import com.project.demo.logic.entity.assignment.Assignment;
import com.project.demo.logic.entity.assignment.AssignmentRepository;
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
@RequestMapping("/task-submissions")
public class TaskSubmissionRestController {

    @Autowired
    private TaskSubmissionRepository taskSubmissionRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAllTaskSubmissions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<TaskSubmission> submissions = taskSubmissionRepository.findAll(pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(submissions.getTotalPages());
        meta.setTotalElements(submissions.getTotalElements());
        meta.setPageNumber(submissions.getNumber() + 1);
        meta.setPageSize(submissions.getSize());

        return new GlobalResponseHandler().handleResponse("Entregas de tareas obtenidas con éxito",
                submissions.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/{submissionId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> getTaskSubmissionById(@PathVariable Long submissionId, HttpServletRequest request) {
        Optional<TaskSubmission> found = taskSubmissionRepository.findById(submissionId);
        if (found.isPresent()) {
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            return new GlobalResponseHandler().handleResponse("Entrega de tarea obtenida con éxito",
                    found.get(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Entrega de tarea " + submissionId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/assignment/{assignmentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getByAssignmentId(@PathVariable Long assignmentId,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<TaskSubmission> submissions = taskSubmissionRepository.findByAssignmentId(assignmentId, pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(submissions.getTotalPages());
        meta.setTotalElements(submissions.getTotalElements());
        meta.setPageNumber(submissions.getNumber() + 1);
        meta.setPageSize(submissions.getSize());

        return new GlobalResponseHandler().handleResponse("Entregas de la asignación obtenidas con éxito",
                submissions.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> getByStudentId(@PathVariable Long studentId,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<TaskSubmission> submissions = taskSubmissionRepository.findByStudentId(studentId, pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(submissions.getTotalPages());
        meta.setTotalElements(submissions.getTotalElements());
        meta.setPageNumber(submissions.getNumber() + 1);
        meta.setPageSize(submissions.getSize());

        return new GlobalResponseHandler().handleResponse("Entregas del estudiante obtenidas con éxito",
                submissions.getContent(), HttpStatus.OK, meta);
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> createTaskSubmission(@RequestBody TaskSubmission submission, HttpServletRequest request) {

        Optional<Assignment> assignment = assignmentRepository.findById(submission.getAssignment().getId());
        Optional<User> student = userRepository.findById(submission.getStudent().getId());

        if (assignment.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Asignación no encontrada",
                    HttpStatus.BAD_REQUEST, request);
        }

        if (student.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Estudiante no encontrado",
                    HttpStatus.BAD_REQUEST, request);
        }

        submission.setAssignment(assignment.get());
        submission.setStudent(student.get());

        TaskSubmission saved = (TaskSubmission) taskSubmissionRepository.save(submission);

        return new GlobalResponseHandler().handleResponse("Entrega registrada con éxito",
                saved, HttpStatus.CREATED, request);
    }

    @PutMapping("/{submissionId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> updateTaskSubmission(@PathVariable Long submissionId,
                                                  @RequestBody TaskSubmission submission,
                                                  HttpServletRequest request) {

        Optional<TaskSubmission> found = taskSubmissionRepository.findById(submissionId);
        if (found.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Entrega no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }

        TaskSubmission updated = found.get();
        updated.setComment(submission.getComment());
        updated.setFileUrl(submission.getFileUrl());
        updated.setSubmittedAt(submission.getSubmittedAt());

        taskSubmissionRepository.save(updated);

        return new GlobalResponseHandler().handleResponse("Entrega actualizada con éxito",
                updated, HttpStatus.OK, request);
    }

    @DeleteMapping("/{submissionId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> deleteTaskSubmission(@PathVariable Long submissionId, HttpServletRequest request) {
        Optional<TaskSubmission> found = taskSubmissionRepository.findById(submissionId);
        if (found.isPresent()) {
            taskSubmissionRepository.delete(found.get());
            return new GlobalResponseHandler().handleResponse("Entrega eliminada con éxito",
                    null, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Entrega no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }
}
