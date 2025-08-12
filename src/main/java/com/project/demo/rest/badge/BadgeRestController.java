package com.project.demo.rest.badge;

import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.badge.BadgeRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.rest.badge.dto.BadgeDTO;
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
 * Controlador REST para la gestión de insignias.
 * Permite crear, consultar, actualizar, eliminar y asignar insignias a estudiantes.
 */
@RestController
@RequestMapping("/badges")
public class BadgeRestController {

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Obtiene todas las insignias paginadas.
     *
     * @param page    número de página
     * @param size    tamaño de página
     * @param request petición HTTP
     * @return lista de insignias
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Badge> badgePage = badgeRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(badgePage.getTotalPages());
        meta.setTotalElements(badgePage.getTotalElements());
        meta.setPageNumber(badgePage.getNumber() + 1);
        meta.setPageSize(badgePage.getSize());

        var badgesDto = badgePage.getContent()
                .stream()
                .map(BadgeDTO::fromEntity)
                .collect(Collectors.toList());

        return new GlobalResponseHandler().handleResponse("Insignias obtenidas con éxito",
                badgesDto, HttpStatus.OK, meta);
    }

    /**
     * Obtiene las insignias de un estudiante.
     *
     * @param studentId identificador del estudiante
     * @param page      número de página
     * @param size      tamaño de página
     * @param request   petición HTTP
     * @return lista de insignias del estudiante
     */
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN', 'STUDENT')")
    public ResponseEntity<?> getBadgesByStudent(@PathVariable Long studentId,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Badge> badgePage = badgeRepository.findBadgesByStudentId(studentId, pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(badgePage.getTotalPages());
        meta.setTotalElements(badgePage.getTotalElements());
        meta.setPageNumber(badgePage.getNumber() + 1);
        meta.setPageSize(badgePage.getSize());

        var badgesDto = badgePage.getContent()
                .stream()
                .map(BadgeDTO::fromEntity)
                .collect(Collectors.toList());

        return new GlobalResponseHandler().handleResponse("Insignias del estudiante obtenidas con éxito",
                badgesDto, HttpStatus.OK, meta);
    }

    /**
     * Obtiene una insignia por su identificador.
     *
     * @param badgeId identificador de la insignia
     * @param request petición HTTP
     * @return insignia encontrada
     */
    @GetMapping("/{badgeId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getBadgeById(@PathVariable Long badgeId, HttpServletRequest request) {
        Optional<Badge> foundBadge = badgeRepository.findById(badgeId);
        if (foundBadge.isPresent()) {
            BadgeDTO badgeDto = BadgeDTO.fromEntity(foundBadge.get());
            return new GlobalResponseHandler().handleResponse("Insignia obtenida con éxito",
                    badgeDto, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Insignia " + badgeId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Crea una nueva insignia.
     *
     * @param badge   datos de la insignia
     * @param request petición HTTP
     * @return insignia creada
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> createBadge(@RequestBody Badge badge, HttpServletRequest request) {
        badgeRepository.save(badge);
        BadgeDTO badgeDto = BadgeDTO.fromEntity(badge);
        return new GlobalResponseHandler().handleResponse("Insignia creada con éxito",
                badgeDto, HttpStatus.CREATED, request);
    }

    /**
     * Actualiza una insignia existente.
     *
     * @param badgeId      identificador de la insignia
     * @param updatedBadge datos actualizados
     * @param request      petición HTTP
     * @return insignia actualizada
     */
    @PutMapping("/{badgeId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateBadge(@PathVariable Long badgeId,
                                         @RequestBody Badge updatedBadge,
                                         HttpServletRequest request) {
        Optional<Badge> foundBadge = badgeRepository.findById(badgeId);
        if (foundBadge.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Insignia " + badgeId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }

        Badge badge = foundBadge.get();
        badge.setTitle(updatedBadge.getTitle());
        badge.setDescription(updatedBadge.getDescription());
        badge.setIconUrl(updatedBadge.getIconUrl());

        badgeRepository.save(badge);
        BadgeDTO badgeDto = BadgeDTO.fromEntity(badge);

        return new GlobalResponseHandler().handleResponse("Insignia actualizada correctamente",
                badgeDto, HttpStatus.OK, request);
    }

    /**
     * Elimina una insignia por su identificador.
     *
     * @param badgeId identificador de la insignia
     * @param request petición HTTP
     * @return resultado de la eliminación
     */
    @DeleteMapping("/{badgeId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteBadge(@PathVariable Long badgeId, HttpServletRequest request) {
        Optional<Badge> foundBadge = badgeRepository.findById(badgeId);
        if (foundBadge.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Insignia " + badgeId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
        badgeRepository.delete(foundBadge.get());
        return new GlobalResponseHandler().handleResponse("Insignia eliminada con éxito",
                null, HttpStatus.OK, request);
    }

    /**
     * Remueve una insignia de un estudiante.
     *
     * @param badgeId   identificador de la insignia
     * @param studentId identificador del estudiante
     * @param request   petición HTTP
     * @return insignia actualizada
     */
    @DeleteMapping("/{badgeId}/students/{studentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> removeBadgeFromStudent(@PathVariable Long badgeId,
                                                    @PathVariable Long studentId,
                                                    HttpServletRequest request) {
        Optional<Badge> foundBadge = badgeRepository.findById(badgeId);
        if (foundBadge.isEmpty())
            return new GlobalResponseHandler().handleResponse("Insignia " + badgeId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);

        Optional<User> foundStudent = userRepository.findById(studentId);
        if (foundStudent.isEmpty())
            return new GlobalResponseHandler().handleResponse("Estudiante " + studentId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);

        Badge badge = foundBadge.get();
        User student = foundStudent.get();

        if (!badge.getStudents().remove(student)) {
            return new GlobalResponseHandler().handleResponse(
                    "Estudiante no tiene esta insignia asignada",
                    HttpStatus.BAD_REQUEST,
                    request);
        }

        badgeRepository.save(badge);
        BadgeDTO badgeDto = BadgeDTO.fromEntity(badge);

        return new GlobalResponseHandler().handleResponse("Insignia removida con éxito",
                badgeDto, HttpStatus.OK, request);
    }

    /**
     * Asigna una insignia a un estudiante.
     *
     * @param badgeId   identificador de la insignia
     * @param studentId identificador del estudiante
     * @param request   petición HTTP
     * @return insignia actualizada
     */
    @PostMapping("/{badgeId}/students/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> assignBadgeToStudent(@PathVariable Long badgeId,
                                                  @PathVariable Long studentId,
                                                  HttpServletRequest request) {
        Optional<Badge> badgeOpt = badgeRepository.findById(badgeId);
        if (badgeOpt.isEmpty())
            return new GlobalResponseHandler().handleResponse("Insignia " + badgeId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);

        Optional<User> userOpt = userRepository.findById(studentId);
        if (userOpt.isEmpty())
            return new GlobalResponseHandler().handleResponse("Estudiante " + studentId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);

        Badge badge = badgeOpt.get();
        User user = userOpt.get();

        if (badge.getStudents().contains(user)) {
            return new GlobalResponseHandler().handleResponse("Estudiante ya tiene esta insignia asignada",
                    HttpStatus.BAD_REQUEST, request);
        }

        badge.getStudents().add(user);
        badgeRepository.save(badge);

        BadgeDTO badgeDto = BadgeDTO.fromEntity(badge);

        return new GlobalResponseHandler().handleResponse("Insignia asignada con éxito",
                badgeDto, HttpStatus.OK, request);
    }
}
