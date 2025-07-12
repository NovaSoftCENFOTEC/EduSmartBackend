package com.project.demo.rest.group;

import com.project.demo.logic.entity.course.Course;
import com.project.demo.logic.entity.course.CourseRepository;
import com.project.demo.logic.entity.group.Group;
import com.project.demo.logic.entity.group.GroupRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
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
@RequestMapping("/groups")
public class GroupRestController {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Group> groupPage = groupRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(groupPage.getTotalPages());
        meta.setTotalElements(groupPage.getTotalElements());
        meta.setPageNumber(groupPage.getNumber() + 1);
        meta.setPageSize(groupPage.getSize());

        return new GlobalResponseHandler().handleResponse("Grupos obtenidos con exito",
                groupPage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/course/{courseId}/groups")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getGroupsByCourse(@PathVariable Long courseId,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {

        Optional<Course> foundCourse = courseRepository.findById(courseId);
        if (foundCourse.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Group> groupPage = groupRepository.findGroupsByCourseId(courseId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(groupPage.getTotalPages());
            meta.setTotalElements(groupPage.getTotalElements());
            meta.setPageNumber(groupPage.getNumber() + 1);
            meta.setPageSize(groupPage.getSize());

            return new GlobalResponseHandler().handleResponse("Grupos obtenidos con exito por ID curso",
                    groupPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Curso " + courseId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/teacher/{teacherId}/groups")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getGroupsByTeacher(@PathVariable Long teacherId,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                HttpServletRequest request) {

        Optional<User> foundTeacher = userRepository.findById(teacherId);
        if (foundTeacher.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Group> groupPage = groupRepository.findGroupsByTeacherId(teacherId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(groupPage.getTotalPages());
            meta.setTotalElements(groupPage.getTotalElements());
            meta.setPageNumber(groupPage.getNumber() + 1);
            meta.setPageSize(groupPage.getSize());

            return new GlobalResponseHandler().handleResponse("Grupos obtenidos con exito por ID docente",
                    groupPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Docente " + teacherId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping("/course/{courseId}/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> addGroup(@PathVariable Long courseId,
                                      @PathVariable Long teacherId,
                                      @RequestBody Group group,
                                      HttpServletRequest request) {
        Optional<Course> foundCourse = courseRepository.findById(courseId);
        if (foundCourse.isPresent()) {
            group.setCourse(foundCourse.get());

            Optional<User> foundTeacher = userRepository.findById(teacherId);
            if (foundTeacher.isPresent()) {
                group.setTeacher(foundTeacher.get());

                groupRepository.save(group);
            } else {
                return new GlobalResponseHandler().handleResponse("Docente " + teacherId + " no encontrado",
                        HttpStatus.NOT_FOUND, request);
            }
        } else {
            return new GlobalResponseHandler().handleResponse("Curso " + courseId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
        return new GlobalResponseHandler().handleResponse("Grupo creado con exito",
                group, HttpStatus.OK, request);
    }

    @PostMapping("/{groupId}/students/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> addStudentToGroup(@PathVariable Long groupId,
                                               @PathVariable Long studentId,
                                               HttpServletRequest request) {
        Optional<Group> foundGroup = groupRepository.findById(groupId);
        if (foundGroup.isPresent()) {
            Optional<User> foundStudent = userRepository.findById(studentId);
            if (foundStudent.isPresent()) {
                Group group = foundGroup.get();
                group.getStudents().add(foundStudent.get());
                groupRepository.save(group);

                return new GlobalResponseHandler().handleResponse("Estudiante añadido al grupo con éxito",
                        group, HttpStatus.OK, request);
            } else {
                return new GlobalResponseHandler().handleResponse("Estudiante " + studentId + " no encontrado",
                        HttpStatus.NOT_FOUND, request);
            }
        } else {
            return new GlobalResponseHandler().handleResponse("Grupo " + groupId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }


    @PutMapping("/{groupId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateGroup(@PathVariable Long groupId, @RequestBody Group group, HttpServletRequest request) {
        Optional<Group> foundGroup = groupRepository.findById(groupId);
        if (foundGroup.isPresent()) {
            Group updatedGroup = foundGroup.get();
            updatedGroup.setName(group.getName());
            updatedGroup.setCourse(group.getCourse());
            updatedGroup.setTeacher(group.getTeacher());
            groupRepository.save(updatedGroup);

            return new GlobalResponseHandler().handleResponse("Grupo actualizado con exito",
                    group, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Grupo " + groupId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId, HttpServletRequest request) {
        Optional<Group> foundGroup = groupRepository.findById(groupId);
        if (foundGroup.isPresent()) {
            groupRepository.delete(foundGroup.get());
            return new GlobalResponseHandler().handleResponse("Grupo eliminado con exito",
                    null, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Grupo " + groupId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{groupId}/students/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> removeStudentFromGroup(@PathVariable Long groupId,
                                                    @PathVariable Long studentId,
                                                    HttpServletRequest request) {
        Optional<Group> foundGroup = groupRepository.findById(groupId);
        if (foundGroup.isPresent()) {
            Group group = foundGroup.get();
            Optional<User> foundStudent = userRepository.findById(studentId);
            if (foundStudent.isPresent()) {
                group.getStudents().remove(foundStudent.get());
                groupRepository.save(group);

                return new GlobalResponseHandler().handleResponse("Estudiante eliminado del grupo con éxito",
                        group, HttpStatus.OK, request);
            } else {
                return new GlobalResponseHandler().handleResponse("Estudiante " + studentId + " no encontrado",
                        HttpStatus.NOT_FOUND, request);
            }
        } else {
            return new GlobalResponseHandler().handleResponse("Grupo " + groupId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }
}
