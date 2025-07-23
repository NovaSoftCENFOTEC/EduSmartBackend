package com.project.demo.rest.course;

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
@RequestMapping("/courses")
public class CourseRestController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Course> coursePage = courseRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(coursePage.getTotalPages());
        meta.setTotalElements(coursePage.getTotalElements());
        meta.setPageNumber(coursePage.getNumber() + 1);
        meta.setPageSize(coursePage.getSize());

        return new GlobalResponseHandler().handleResponse("Cursos obtenidos correctamente",
                coursePage.getContent(), HttpStatus.OK, meta);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> addCourse(@RequestBody Course course, HttpServletRequest request) {
        courseRepository.save(course);
        return new GlobalResponseHandler().handleResponse("Curso creado con éxito",
                course, HttpStatus.OK, request);
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateCourse(@PathVariable Long courseId, @RequestBody Course course, HttpServletRequest request) {
        Optional<Course> foundCourse = courseRepository.findById(courseId);
        if (foundCourse.isPresent()) {
            Course updatedCourse = foundCourse.get();
            updatedCourse.setCode(course.getCode());
            updatedCourse.setTitle(course.getTitle());
            updatedCourse.setDescription(course.getDescription());
            courseRepository.save(updatedCourse);

            return new GlobalResponseHandler().handleResponse("Curso editado con éxito",
                    course, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Curso " + courseId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId, HttpServletRequest request) {
        Optional<Course> foundCourse = courseRepository.findById(courseId);
        if (foundCourse.isPresent()) {
            courseRepository.delete(foundCourse.get());
            return new GlobalResponseHandler().handleResponse("Curso eliminado con éxito",
                    foundCourse.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Curso " + courseId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }


}
