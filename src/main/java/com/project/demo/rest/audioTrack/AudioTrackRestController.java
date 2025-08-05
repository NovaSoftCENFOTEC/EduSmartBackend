package com.project.demo.rest.audioTrack;

import com.project.demo.logic.entity.audioTrack.AudioTrack;
import com.project.demo.logic.entity.audioTrack.AudioTrackRepository;
import com.project.demo.logic.entity.audioTrack.GoogleCloudTTSService;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.story.Story;
import com.project.demo.logic.entity.story.StoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/audio-tracks")
public class AudioTrackRestController {
    @Autowired
    private AudioTrackRepository audioTrackRepository;

    @Autowired
    private GoogleCloudTTSService googleCloudTTSService;

    @Autowired
    private StoryRepository storyRepository;

    @GetMapping("/story/{storyId}/audio-tracks")
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAudioTracksByStory(@PathVariable Long storyId,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   HttpServletRequest request) {
        Optional<Story> foundStory = storyRepository.findById(storyId);
        if (foundStory.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<AudioTrack> audioTrackPage = audioTrackRepository.findAudioTracksByStoryId(storyId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(audioTrackPage.getTotalPages());
            meta.setTotalElements(audioTrackPage.getTotalElements());
            meta.setPageNumber(audioTrackPage.getNumber() + 1);
            meta.setPageSize(audioTrackPage.getSize());

            return new GlobalResponseHandler().handleResponse("Pistas de audio obtenidas correctamente por ID de historia",
                    audioTrackPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("Story " + storyId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping("/story/{storyId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> addAudioTrack(@PathVariable Long storyId, @RequestBody AudioTrack audioTrack, HttpServletRequest request) {
        Optional<Story> foundStory = storyRepository.findById(storyId);
        if (foundStory.isPresent()) {
            audioTrack.setStory(foundStory.get());
            audioTrackRepository.save(audioTrack);
            return new GlobalResponseHandler().handleResponse("Pista de audio creada con éxito",
                    audioTrack, HttpStatus.CREATED, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Story " + storyId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping("/tts")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> generateAudioTrackFromText(@RequestBody String text) {
        try {
            byte[] audioData = googleCloudTTSService.convertTextToMp3(text);
            ByteArrayResource resource = new ByteArrayResource(audioData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"speech.mp3\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(audioData.length)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GlobalResponseHandler().handleResponse(
                    "Error generating audio track: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null));
        }
    }


    @PutMapping("/{audioTrackId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateAudioTrack(@PathVariable Long audioTrackId, @RequestBody AudioTrack audioTrack, HttpServletRequest request) {
        Optional<AudioTrack> foundAudioTrack = audioTrackRepository.findById(audioTrackId);
        if (foundAudioTrack.isPresent()) {
            AudioTrack updatedAudioTrack = foundAudioTrack.get();
            updatedAudioTrack.setTitle(audioTrack.getTitle());
            updatedAudioTrack.setVoiceType(audioTrack.getVoiceType());
            updatedAudioTrack.setUrl(audioTrack.getUrl());
            updatedAudioTrack.setDuration(audioTrack.getDuration());
            updatedAudioTrack.setStory(updatedAudioTrack.getStory());
            audioTrackRepository.save(updatedAudioTrack);

            return new GlobalResponseHandler().handleResponse("Pista de audio actualizada con éxito",
                    updatedAudioTrack, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Audio track " + audioTrackId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @DeleteMapping("/{audioTrackId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteAudioTrack(@PathVariable Long audioTrackId, HttpServletRequest request) {
        Optional<AudioTrack> foundAudioTrack = audioTrackRepository.findById(audioTrackId);
        if (foundAudioTrack.isPresent()) {
            audioTrackRepository.delete(foundAudioTrack.get());
            return new GlobalResponseHandler().handleResponse("Pista de audio eliminada con éxito",
                    foundAudioTrack.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Audio track " + audioTrackId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }
}
