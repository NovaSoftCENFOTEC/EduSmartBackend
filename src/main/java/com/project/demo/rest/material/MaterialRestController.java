package com.project.demo.rest.material;

import com.project.demo.logic.entity.course.Course;
import com.project.demo.logic.entity.course.CourseRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.material.Material;
import com.project.demo.logic.entity.material.MaterialRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.rest.material.dto.MaterialUpdateDto;
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

/**
 * Controlador REST para la gestión de materiales educativos.
 * Permite crear, consultar, actualizar y eliminar materiales asociados a cursos y profesores.
 */
@RestController
@RequestMapping("/materials")
public class MaterialRestController {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Obtiene los materiales asociados a un curso.
     *
     * @param courseId identificador del curso
     * @param page     número de página
     * @param size     tamaño de página
     * @param request  petición HTTP
     * @return lista de materiales del curso
     */
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

    /**
     * Obtiene los materiales asociados a un profesor.
     *
     * @param teacherId identificador del profesor
     * @param page      número de página
     * @param size      tamaño de página
     * @param request   petición HTTP
     * @return lista de materiales del profesor
     */
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

    /**
     * Crea un nuevo material asociado a un curso y profesor.
     *
     * @param courseId  identificador del curso
     * @param teacherId identificador del profesor
     * @param material  datos del material
     * @param request   petición HTTP
     * @return material creado
     */
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
                return new GlobalResponseHandler().handleResponse("Material creado con éxito",
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

    /**
     * Actualiza los datos de un material existente.
     *
     * @param materialId  identificador del material
     * @param materialDto datos actualizados
     * @param request     petición HTTP
     * @return material actualizado
     */
    @PutMapping("/{materialId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateMaterial(@PathVariable Long materialId, @RequestBody MaterialUpdateDto materialDto, HttpServletRequest request) {
        Optional<Material> foundMaterial = materialRepository.findById(materialId);
        if (foundMaterial.isPresent()) {
            Material updatedMaterial = foundMaterial.get();

            updatedMaterial.setName(materialDto.getName());
            updatedMaterial.setFileUrl(materialDto.getFileUrl());

            if (materialDto.getCourseId() != null) {
                Optional<Course> course = courseRepository.findById(materialDto.getCourseId());
                course.ifPresent(updatedMaterial::setCourse);
            }

            if (materialDto.getTeacherId() != null) {
                Optional<User> teacher = userRepository.findById(materialDto.getTeacherId());
                teacher.ifPresent(updatedMaterial::setTeacher);
            }

            materialRepository.save(updatedMaterial);

            return new GlobalResponseHandler().handleResponse("Material actualizado con éxito",
                    updatedMaterial, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Material " + materialId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Elimina un material por su identificador.
     *
     * @param materialId identificador del material
     * @param request    petición HTTP
     * @return resultado de la eliminación
     */
    @DeleteMapping("/{materialId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long materialId, HttpServletRequest request) {
        Optional<Material> foundMaterial = materialRepository.findById(materialId);
        if (foundMaterial.isPresent()) {
            materialRepository.delete(foundMaterial.get());
            return new GlobalResponseHandler().handleResponse("Material eliminado con éxito",
                    foundMaterial.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Material " + materialId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }
}
