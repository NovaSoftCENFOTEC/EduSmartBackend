package com.project.demo.rest.story;

import com.project.demo.logic.entity.course.Course;
import com.project.demo.logic.entity.course.CourseRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.story.Story;
import com.project.demo.logic.entity.story.StoryAudioTrackService;
import com.project.demo.logic.entity.story.StoryRepository;
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
 * Controlador REST para la gestión de historias educativas.
 * Permite crear, consultar, actualizar y eliminar historias asociadas a cursos.
 */
@RestController
@RequestMapping("/stories")
public class StoryRestController {
    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StoryAudioTrackService storyAudioTrackService;

    /**
     * Obtiene las historias asociadas a un curso.
     * @param courseId identificador del curso
     * @param page número de página
     * @param size tamaño de página
     * @param request petición HTTP
     * @return lista de historias del curso
     */
    @GetMapping("/course/{courseId}/stories")
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getStoriesByCourse(@PathVariable Long courseId,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  HttpServletRequest request) {

        Optional<Course> foundCourse = courseRepository.findById(courseId);
        if (foundCourse.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Story> storyPage = storyRepository.findStoriesByCourseId(courseId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(storyPage.getTotalPages());
            meta.setTotalElements(storyPage.getTotalElements());
            meta.setPageNumber(storyPage.getNumber() + 1);
            meta.setPageSize(storyPage.getSize());

            return new GlobalResponseHandler().handleResponse("Historias obtenidas correctamente por ID de curso",
                    storyPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Curso " + courseId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Crea una nueva historia asociada a un curso.
     * @param courseId identificador del curso
     * @param story datos de la historia
     * @param request petición HTTP
     * @return historia creada
     */
    @PostMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> addStory(@PathVariable Long courseId, @RequestBody Story story, HttpServletRequest request) {
        Optional<Course> foundCourse = courseRepository.findById(courseId);
        if (foundCourse.isPresent()) {
            story.setCourse(foundCourse.get());
            storyRepository.save(story);

            try {
                storyAudioTrackService.generateAndSaveAudioTracksForStory(story);
            } catch (Exception e) {
                return new GlobalResponseHandler().handleResponse("Story created but failed to generate audio tracks: " + e.getMessage(),
                        story, HttpStatus.CREATED, request);
            }

            return new GlobalResponseHandler().handleResponse("Historia creada con éxito",
                    story, HttpStatus.CREATED, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Curso " + courseId + " no encontrado",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Actualiza los datos de una historia existente.
     * @param storyId identificador de la historia
     * @param story datos actualizados
     * @param request petición HTTP
     * @return historia actualizada
     */
    @PutMapping("/{storyId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateStory(@PathVariable Long storyId, @RequestBody Story story, HttpServletRequest request) {
        Optional<Story> foundStory = storyRepository.findById(storyId);
        if (foundStory.isPresent()) {
            Story updatedStory = foundStory.get();
            updatedStory.setTitle(story.getTitle());
            updatedStory.setContent(story.getContent());
            updatedStory.setCourse(story.getCourse());
            storyRepository.save(updatedStory);

            try {
                storyAudioTrackService.updateAudioTracksForStory(updatedStory);
            } catch (Exception e) {
                return new GlobalResponseHandler().handleResponse("Story updated but failed to update audio tracks: " + e.getMessage(),
                        updatedStory, HttpStatus.OK, request);
            }

            return new GlobalResponseHandler().handleResponse("Historia editada con éxito",
                    updatedStory, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Historia " + storyId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    /**
     * Elimina una historia por su identificador.
     * @param storyId identificador de la historia
     * @param request petición HTTP
     * @return resultado de la eliminación
     */
    @DeleteMapping("/{storyId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteStory(@PathVariable Long storyId, HttpServletRequest request) {
        Optional<Story> foundStory = storyRepository.findById(storyId);
        if (foundStory.isPresent()) {
            storyAudioTrackService.deleteAudioTracksForStory(foundStory.get());
            storyRepository.delete(foundStory.get());
            return new GlobalResponseHandler().handleResponse("Historia eliminada con éxito",
                    foundStory.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Historia " + storyId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }
}
