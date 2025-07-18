package com.project.demo.rest.assignment;

import com.project.demo.logic.entity.assignment.Assignment;
import com.project.demo.logic.entity.assignment.AssignmentRepository;
import com.project.demo.logic.entity.group.Group;
import com.project.demo.logic.entity.group.GroupRepository;
import com.project.demo.logic.entity.course.Course;
import com.project.demo.logic.entity.course.CourseRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
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

@RestController
@RequestMapping("/assignments")
public class AssignmentRestController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAllAssignments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Assignment> assignmentPage = assignmentRepository.findAll(pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(assignmentPage.getTotalPages());
        meta.setTotalElements(assignmentPage.getTotalElements());
        meta.setPageNumber(assignmentPage.getNumber() + 1);
        meta.setPageSize(assignmentPage.getSize());

        return new GlobalResponseHandler().handleResponse("Asignaciones obtenidas con éxito",
                assignmentPage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/{assignmentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAssignmentById(@PathVariable Long assignmentId, HttpServletRequest request) {

        Optional<Assignment> foundAssignment = assignmentRepository.findById(assignmentId);
        if (foundAssignment.isPresent()) {
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            return new GlobalResponseHandler().handleResponse("Asignación obtenida con éxito",
                    foundAssignment.get(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Asignación " + assignmentId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }
    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> getAssignmentsByGroupId(@PathVariable Long groupId,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     HttpServletRequest request) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Grupo " + groupId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Assignment> assignmentPage = assignmentRepository.findByGroup(groupOptional.get(), pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(assignmentPage.getTotalPages());
        meta.setTotalElements(assignmentPage.getTotalElements());
        meta.setPageNumber(assignmentPage.getNumber() + 1);
        meta.setPageSize(assignmentPage.getSize());

        return new GlobalResponseHandler().handleResponse("Asignaciones obtenidas con éxito para el grupo " + groupId,
                assignmentPage.getContent(), HttpStatus.OK, meta);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> createAssignment(@RequestBody Assignment assignment, HttpServletRequest request) {
        Optional<Group> group = groupRepository.findById(assignment.getGroup().getId());
        if (group.isPresent()) {
            assignment.setGroup(group.get());
            Assignment savedAssignment = assignmentRepository.save(assignment);
            return new GlobalResponseHandler().handleResponse("Asignación creada con éxito",
                    savedAssignment, HttpStatus.CREATED, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Grupo " + assignment.getGroup().getId() + " no encontrado",
                    HttpStatus.BAD_REQUEST, request);
        }
    }

    @PostMapping("/group/{groupId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> createAssignmentForGroup(@PathVariable Long groupId,
                                                      @RequestBody Assignment assignment,
                                                      HttpServletRequest request) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            assignment.setGroup(group.get());
            Assignment savedAssignment = assignmentRepository.save(assignment);

            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            return new GlobalResponseHandler().handleResponse("Asignación creada con éxito en el grupo " + groupId,
                    savedAssignment, HttpStatus.CREATED, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Grupo " + groupId + " no encontrado",
                    HttpStatus.BAD_REQUEST, request);
        }
    }

    @PutMapping("/{assignmentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateAssignment(@PathVariable Long assignmentId,
                                              @RequestBody Assignment assignment,
                                              HttpServletRequest request) {

        Optional<Assignment> foundAssignment = assignmentRepository.findById(assignmentId);
        if (foundAssignment.isPresent()) {
            Assignment updated = foundAssignment.get();
            updated.setTitle(assignment.getTitle());
            updated.setDescription(assignment.getDescription());
            updated.setType(assignment.getType());
            updated.setDueDate(assignment.getDueDate());
            updated.setGroup(assignment.getGroup());
            assignmentRepository.save(updated);

            return new GlobalResponseHandler().handleResponse("Asignación actualizada con éxito",
                    updated, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Asignación " + assignmentId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{assignmentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long assignmentId, HttpServletRequest request) {
        Optional<Assignment> foundAssignment = assignmentRepository.findById(assignmentId);
        if (foundAssignment.isPresent()) {
            assignmentRepository.delete(foundAssignment.get());
            return new GlobalResponseHandler().handleResponse("Asignación eliminada con éxito",
                    null, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Asignación " + assignmentId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }
}