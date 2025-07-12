package com.project.demo.rest.illustration;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.illustration.Illustration;
import com.project.demo.logic.entity.illustration.IllustrationRepository;
import com.project.demo.logic.entity.story.Story;
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

@RestController
@RequestMapping("/illustrations")
public class IllustrationRestController {
    @Autowired
    private IllustrationRepository illustrationRepository;

    @Autowired
    private StoryRepository storyRepository;

    @GetMapping("/story/{storyId}/illustrations")
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getIllustrationsByStory(@PathVariable Long storyId,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     HttpServletRequest request) {
        Optional<Story> foundStory = storyRepository.findById(storyId);
        if (foundStory.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Illustration> illustrationPage = illustrationRepository.findIllustrationsByStoryId(storyId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(illustrationPage.getTotalPages());
            meta.setTotalElements(illustrationPage.getTotalElements());
            meta.setPageNumber(illustrationPage.getNumber() + 1);
            meta.setPageSize(illustrationPage.getSize());

            return new GlobalResponseHandler().handleResponse("Illustraciones obtenidas correctamente por ID de historia",
                    illustrationPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Historia " + storyId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping("/story/{storyId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> addIllustration(@PathVariable Long storyId, @RequestBody Illustration illustration, HttpServletRequest request) {
        Optional<Story> foundStory = storyRepository.findById(storyId);
        if (foundStory.isPresent()) {
            illustration.setStory(foundStory.get());
            illustrationRepository.save(illustration);
            return new GlobalResponseHandler().handleResponse("Ilustración creada con éxito",
                    illustration, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Historia " + storyId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PutMapping("/{illustrationId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateIllustration(@PathVariable Long illustrationId, @RequestBody Illustration illustration, HttpServletRequest request) {
        Optional<Illustration> foundIllustration = illustrationRepository.findById(illustrationId);
        if (foundIllustration.isPresent()) {
            Illustration updatedIllustration = foundIllustration.get();
            updatedIllustration.setTitle(illustration.getTitle());
            updatedIllustration.setUrl(illustration.getUrl());
            updatedIllustration.setAltText(illustration.getAltText());
            updatedIllustration.setStory(updatedIllustration.getStory());
            illustrationRepository.save(updatedIllustration);

            return new GlobalResponseHandler().handleResponse("Ilustración actualizada con éxito",
                    updatedIllustration, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Ilustración " + illustrationId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{illustrationId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteIllustration(@PathVariable Long illustrationId, HttpServletRequest request) {
        Optional<Illustration> foundIllustration = illustrationRepository.findById(illustrationId);
        if (foundIllustration.isPresent()) {
            illustrationRepository.delete(foundIllustration.get());
            return new GlobalResponseHandler().handleResponse("Ilustración eliminada con éxito",
                    foundIllustration.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Ilustración " + illustrationId + " no encontrada",
                    HttpStatus.NOT_FOUND, request);
        }
    }

}
