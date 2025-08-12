package com.project.demo.rest.school;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.school.School;
import com.project.demo.logic.entity.school.SchoolRepository;
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
 * Controlador REST para la gestión de escuelas.
 * Permite crear, consultar, actualizar y eliminar escuelas.
 */
@RestController
@RequestMapping("/schools")
public class SchoolRestController {

    @Autowired
    private SchoolRepository schoolRepository;

    /**
     * Obtiene todas las escuelas paginadas.
     * @param page número de página
     * @param size tamaño de página
     * @param request petición HTTP
     * @return lista de escuelas
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<School> schoolPage = schoolRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(schoolPage.getTotalPages());
        meta.setTotalElements(schoolPage.getTotalElements());
        meta.setPageNumber(schoolPage.getNumber() + 1);
        meta.setPageSize(schoolPage.getSize());

        return new GlobalResponseHandler().handleResponse("Escuelas obtenidas con éxito",
                schoolPage.getContent(), HttpStatus.OK, meta);
    }

    /**
     * Obtiene una escuela por su identificador.
     * @param schoolId identificador de la escuela
     * @param request petición HTTP
     * @return escuela encontrada
     */
    @GetMapping("/{schoolId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getSchoolById(@PathVariable Long schoolId, HttpServletRequest request) {
        Optional<School> foundSchool = schoolRepository.findById(schoolId);
        if (foundSchool.isPresent()) {
            return new GlobalResponseHandler().handleResponse(
                    "Escuela obtenida con éxito",
                    foundSchool.get(),
                    HttpStatus.OK,
                    request);
        } else {
            return new GlobalResponseHandler().handleResponse(
                    "Escuela " + schoolId + " no encontrada",
                    HttpStatus.NOT_FOUND,
                    request);
        }
    }

    /**
     * Crea una nueva escuela.
     * @param school datos de la escuela
     * @param request petición HTTP
     * @return escuela creada
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> addSchool(@RequestBody School school, HttpServletRequest request) {
        schoolRepository.save(school);
        return new GlobalResponseHandler().handleResponse("Escuela creada con éxito",
                school, HttpStatus.OK, request);
    }

    /**
     * Actualiza los datos de una escuela existente.
     * @param schoolId identificador de la escuela
     * @param school datos actualizados
     * @param request petición HTTP
     * @return escuela actualizada
     */
    @PutMapping("/{schoolId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateSchool(@PathVariable Long schoolId, @RequestBody School school, HttpServletRequest request) {
        Optional<School> foundSchool = schoolRepository.findById(schoolId);
        if (foundSchool.isPresent()) {
            School updatedSchool = foundSchool.get();
            updatedSchool.setName(school.getName());
            updatedSchool.setDomain(school.getDomain());
            schoolRepository.save(updatedSchool);
            return new GlobalResponseHandler().handleResponse("Escuela editada con éxito",
                    school, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Escuela " + schoolId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Elimina una escuela por su identificador.
     * @param schoolId identificador de la escuela
     * @param request petición HTTP
     * @return resultado de la eliminación
     */
    @DeleteMapping("/{schoolId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteSchool(@PathVariable Long schoolId, HttpServletRequest request) {
        Optional<School> foundSchool = schoolRepository.findById(schoolId);
        if (foundSchool.isPresent()) {
            schoolRepository.deleteById(schoolId);
            return new GlobalResponseHandler().handleResponse("Escuela eliminada con éxito",
                    foundSchool.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Escuela " + schoolId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

}
