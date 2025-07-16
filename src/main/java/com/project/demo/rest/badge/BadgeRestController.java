package com.project.demo.rest.badge;

import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.badge.BadgeRepository;
import com.project.demo.logic.entity.cloudinary.CloudinaryService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/badges")
public class BadgeRestController {

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

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

        return new GlobalResponseHandler().handleResponse("Insignias obtenidas con éxito",
                badgePage.getContent(), HttpStatus.OK, meta);
    }

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

        return new GlobalResponseHandler().handleResponse("Insignias del estudiante obtenidas con éxito",
                badgePage.getContent(), HttpStatus.OK, meta);
    }


    @GetMapping("/{badgeId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getBadgeById(@PathVariable Long badgeId, HttpServletRequest request) {
        Optional<Badge> foundBadge = badgeRepository.findById(badgeId);
        if (foundBadge.isPresent()) {
            return new GlobalResponseHandler().handleResponse("Insignia obtenida con éxito",
                    foundBadge.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Insignia " + badgeId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> createBadge(@RequestBody Badge badge, HttpServletRequest request) {
        badgeRepository.save(badge);
        return new GlobalResponseHandler().handleResponse("Insignia creada con éxito",
                badge, HttpStatus.CREATED, request);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> createBadgeWithUpload(@RequestParam("title") String title,
                                                   @RequestParam("description") String description,
                                                   @RequestParam("image") MultipartFile image,
                                                   HttpServletRequest request) {
        try {

            String imageUrl = cloudinaryService.uploadImage(image);

            Badge badge = new Badge();
            badge.setTitle(title);
            badge.setDescription(description);
            badge.setIconUrl(imageUrl);

            badgeRepository.save(badge);

            return new GlobalResponseHandler().handleResponse("Insignia creada con imagen correctamente",
                    badge, HttpStatus.CREATED, request);
        } catch (IOException e) {
            return new GlobalResponseHandler().handleResponse("Error al subir la imagen",
                    HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
    }


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
        return new GlobalResponseHandler().handleResponse("Insignia removida con éxito",
                badge, HttpStatus.OK, request);
    }

    @PutMapping("/{badgeId}/upload")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateBadgeWithImage(@PathVariable Long badgeId,
                                                  @RequestParam("title") String title,
                                                  @RequestParam("description") String description,
                                                  @RequestParam(value = "image", required = false) MultipartFile image,
                                                  HttpServletRequest request) {
        Optional<Badge> foundBadge = badgeRepository.findById(badgeId);
        if (foundBadge.isEmpty()) {
            return new GlobalResponseHandler().handleResponse("Insignia " + badgeId + " no encontrada", HttpStatus.NOT_FOUND, request);
        }

        Badge badge = foundBadge.get();
        badge.setTitle(title);
        badge.setDescription(description);

        if (image != null && !image.isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadImage(image);
                badge.setIconUrl(imageUrl);
            } catch (IOException e) {
                return new GlobalResponseHandler().handleResponse("Error al subir la imagen", HttpStatus.INTERNAL_SERVER_ERROR, request);
            }
        }

        badgeRepository.save(badge);
        return new GlobalResponseHandler().handleResponse("Insignia actualizada correctamente", badge, HttpStatus.OK, request);
    }


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
}
