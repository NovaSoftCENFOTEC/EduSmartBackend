package com.project.demo.rest.material;

import com.project.demo.logic.entity.course.Course;
import com.project.demo.logic.entity.course.CourseRepository;
import com.project.demo.logic.entity.group.Group;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.material.Material;
import com.project.demo.logic.entity.material.MaterialRepository;
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
@RequestMapping("/materials")
public class MaterialRestController {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/course/{courseId}/materials")
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getMaterialsByCourse(@PathVariable Long courseId,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {

        Optional<Course> foundCourse = courseRepository.findById(courseId);
        if (foundCourse.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Material> materialPage = materialRepository.findMaterialsByCourseId(courseId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(materialPage.getTotalPages());
            meta.setTotalElements(materialPage.getTotalElements());
            meta.setPageNumber(materialPage.getNumber() + 1);
            meta.setPageSize(materialPage.getSize());

            return new GlobalResponseHandler().handleResponse("Materiales obtenidos correctamente por ID de curso",
                    materialPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Curso " + courseId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/teacher/{teacherId}/materials")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getMaterialsByTeacher(@PathVariable Long teacherId,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   HttpServletRequest request) {

        Optional<User> foundUser = userRepository.findById(teacherId);
        if (foundUser.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Material> materialPage = materialRepository.findMaterialsByTeacherId(teacherId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(materialPage.getTotalPages());
            meta.setTotalElements(materialPage.getTotalElements());
            meta.setPageNumber(materialPage.getNumber() + 1);
            meta.setPageSize(materialPage.getSize());

            return new GlobalResponseHandler().handleResponse("Materiales obtenidos correctamente por ID de profesor",
                    materialPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Profesor " + teacherId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping("/course/{courseId}/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> addMaterial(@PathVariable Long courseId,
                                         @PathVariable Long teacherId,
                                         @RequestBody Material material,
                                         HttpServletRequest request) {
        Optional<Course> foundCourse = courseRepository.findById(courseId);
        if (foundCourse.isPresent()) {
            material.setCourse(foundCourse.get());

            Optional<User> foundTeacher = userRepository.findById(teacherId);
            if (foundTeacher.isPresent()) {
                material.setTeacher(foundTeacher.get());
                materialRepository.save(material);
                return new GlobalResponseHandler().handleResponse("Material creado con exito",
                        material, HttpStatus.OK, request);

            } else {
                return new GlobalResponseHandler().handleResponse("Docente " + teacherId + " no encontrado",
                        HttpStatus.NOT_FOUND, request);
            }
        } else {
            return new GlobalResponseHandler().handleResponse("Curso " + courseId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PutMapping("/{materialId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateMaterial(@PathVariable Long materialId, @RequestBody Material material, HttpServletRequest request) {
        Optional<Material> foundMaterial = materialRepository.findById(materialId);
        if (foundMaterial.isPresent()) {
            Material updatedMaterial = foundMaterial.get();
            updatedMaterial.setFileUrl(material.getFileUrl());
            updatedMaterial.setUploadedAt(material.getUploadedAt());
            updatedMaterial.setCourse(material.getCourse());
            updatedMaterial.setTeacher(material.getTeacher());
            materialRepository.save(updatedMaterial);

            return new GlobalResponseHandler().handleResponse("Material actualizado con exito",
                    updatedMaterial, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Material " + materialId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{materialId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long materialId, HttpServletRequest request) {
        Optional<Material> foundMaterial = materialRepository.findById(materialId);
        if (foundMaterial.isPresent()) {
            materialRepository.delete(foundMaterial.get());
            return new GlobalResponseHandler().handleResponse("Material eliminado con exito",
                    foundMaterial.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Material " + materialId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }
}
