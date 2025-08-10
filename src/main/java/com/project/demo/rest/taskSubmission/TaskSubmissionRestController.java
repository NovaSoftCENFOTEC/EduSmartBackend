package com.project.demo.rest.taskSubmission;

import com.project.demo.logic.entity.assignment.Assignment;
import com.project.demo.logic.entity.assignment.AssignmentRepository;
import com.project.demo.logic.entity.taskSubmission.TaskSubmission;
import com.project.demo.logic.entity.taskSubmission.TaskSubmissionRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.rest.taskSubmission.dto.TaskSubmissionDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Page<TaskSubmission> submissionsPage = taskSubmissionRepository.findAll(pageable);

        List<TaskSubmissionDTO> dtos = submissionsPage.stream()
                .map(TaskSubmissionDTO::fromEntity)
                .collect(Collectors.toList());

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(submissionsPage.getTotalPages());
        meta.setTotalElements(submissionsPage.getTotalElements());
        meta.setPageNumber(submissionsPage.getNumber() + 1);
        meta.setPageSize(submissionsPage.getSize());

        return new GlobalResponseHandler().handleResponse("Entregas de tareas obtenidas con éxito",
                dtos, HttpStatus.OK, meta);
    }

    @GetMapping("/{submissionId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> getTaskSubmissionById(@PathVariable Long submissionId, HttpServletRequest request) {
        Optional<TaskSubmission> found = taskSubmissionRepository.findById(submissionId);
        if (found.isPresent()) {
            TaskSubmissionDTO dto = TaskSubmissionDTO.fromEntity(found.get());
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            return new GlobalResponseHandler().handleResponse("Entrega de tarea obtenida con éxito",
                    dto, HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Entrega de tarea " + submissionId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/assignment/{assignmentId}")
    @PreAuthorize("hasAnyRole('TEACHER','STUDENT')")
    public ResponseEntity<?> getByAssignmentId(@PathVariable Long assignmentId,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<TaskSubmission> submissionsPage = taskSubmissionRepository.findByAssignmentId(assignmentId, pageable);

        List<TaskSubmissionDTO> dtos = submissionsPage.stream()
                .map(TaskSubmissionDTO::fromEntity)
                .collect(Collectors.toList());

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(submissionsPage.getTotalPages());
        meta.setTotalElements(submissionsPage.getTotalElements());
        meta.setPageNumber(submissionsPage.getNumber() + 1);
        meta.setPageSize(submissionsPage.getSize());

        return new GlobalResponseHandler().handleResponse("Entregas de la asignación obtenidas con éxito",
                dtos, HttpStatus.OK, meta);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> getByStudentId(@PathVariable Long studentId,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<TaskSubmission> submissionsPage = taskSubmissionRepository.findByStudentId(studentId, pageable);

        List<TaskSubmissionDTO> dtos = submissionsPage.stream()
                .map(TaskSubmissionDTO::fromEntity)
                .collect(Collectors.toList());

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(submissionsPage.getTotalPages());
        meta.setTotalElements(submissionsPage.getTotalElements());
        meta.setPageNumber(submissionsPage.getNumber() + 1);
        meta.setPageSize(submissionsPage.getSize());

        return new GlobalResponseHandler().handleResponse("Entregas del estudiante obtenidas con éxito",
                dtos, HttpStatus.OK, meta);
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> createTaskSubmission(@RequestBody TaskSubmissionDTO submissionDTO, HttpServletRequest request) {

        Optional<Assignment> assignment = assignmentRepository.findById(submissionDTO.getAssignmentId());
        Optional<User> student = userRepository.findById(submissionDTO.getStudentId());

        if (assignment.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Asignación no encontrada",
                    HttpStatus.BAD_REQUEST, request);
        }

        if (student.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Estudiante no encontrado",
                    HttpStatus.BAD_REQUEST, request);
        }

        TaskSubmission submission = new TaskSubmission();
        submission.setFileUrl(submissionDTO.getFileUrl());
        submission.setComment(submissionDTO.getComment());
        submission.setSubmittedAt(submissionDTO.getSubmittedAt());
        submission.setAssignment(assignment.get());
        submission.setStudent(student.get());

        TaskSubmission saved = taskSubmissionRepository.save(submission);

        TaskSubmissionDTO savedDto = TaskSubmissionDTO.fromEntity(saved);

        return new GlobalResponseHandler().handleResponse("Entrega registrada con éxito",
                savedDto, HttpStatus.CREATED, request);
    }

    @PutMapping("/{submissionId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> updateTaskSubmission(@PathVariable Long submissionId,
                                                  @RequestBody TaskSubmissionDTO submissionDTO,
                                                  HttpServletRequest request) {

        Optional<TaskSubmission> found = taskSubmissionRepository.findById(submissionId);
        if (found.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Entrega no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }

        TaskSubmission updated = found.get();
        updated.setComment(submissionDTO.getComment());
        updated.setFileUrl(submissionDTO.getFileUrl());
        updated.setSubmittedAt(submissionDTO.getSubmittedAt());

        taskSubmissionRepository.save(updated);

        TaskSubmissionDTO updatedDto = TaskSubmissionDTO.fromEntity(updated);

        return new GlobalResponseHandler().handleResponse("Entrega actualizada con éxito",
                updatedDto, HttpStatus.OK, request);
    }

    @DeleteMapping("/{submissionId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
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


