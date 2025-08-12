package com.project.demo.logic.entity.story;

import com.project.demo.logic.entity.audioTrack.AudioTrack;
import com.project.demo.logic.entity.audioTrack.AudioTrackRepository;
import com.project.demo.logic.entity.audioTrack.GoogleCloudTTSService;
import com.project.demo.logic.entity.audioTrack.VoiceTypeEnum;
import com.project.demo.logic.entity.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para la generación, actualización y eliminación de pistas de audio asociadas a una historia.
 */
@Service
public class StoryAudioTrackService {
    @Autowired
    private GoogleCloudTTSService googleCloudTTSService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private AudioTrackRepository audioTrackRepository;

    /**
     * Genera y guarda las pistas de audio (masculina y femenina) para una historia.
     *
     * @param story historia para la que se generan las pistas
     * @throws Exception si ocurre un error en la generación o guardado
     */
    public void generateAndSaveAudioTracksForStory(Story story) throws Exception {
        String content = story.getContent();
        Long storyId = story.getId();

        byte[] maleAudio = googleCloudTTSService.convertTextToMp3(content, VoiceTypeEnum.MALE);
        byte[] femaleAudio = googleCloudTTSService.convertTextToMp3(content, VoiceTypeEnum.FEMALE);

        String maleUrl = cloudinaryService.uploadAudio(maleAudio, "story_" + storyId + "_male.mp3");
        String femaleUrl = cloudinaryService.uploadAudio(femaleAudio, "story_" + storyId + "_female.mp3");

        AudioTrack maleTrack = new AudioTrack();
        maleTrack.setTitle(story.getTitle() + " (Masuculino)");
        maleTrack.setVoiceType(VoiceTypeEnum.MALE);
        maleTrack.setUrl(maleUrl);
        maleTrack.setStory(story);

        AudioTrack femaleTrack = new AudioTrack();
        femaleTrack.setTitle(story.getTitle() + " (Femenino)");
        femaleTrack.setVoiceType(VoiceTypeEnum.FEMALE);
        femaleTrack.setUrl(femaleUrl);
        femaleTrack.setStory(story);

        audioTrackRepository.save(maleTrack);
        audioTrackRepository.save(femaleTrack);
    }

    /**
     * Actualiza las pistas de audio (masculina y femenina) para una historia.
     *
     * @param story historia para la que se actualizan las pistas
     * @throws Exception si ocurre un error en la actualización
     */
    public void updateAudioTracksForStory(Story story) throws Exception {
        String content = story.getContent();
        Long storyId = story.getId();

        byte[] maleAudio = googleCloudTTSService.convertTextToMp3(content, VoiceTypeEnum.MALE);
        byte[] femaleAudio = googleCloudTTSService.convertTextToMp3(content, VoiceTypeEnum.FEMALE);

        String maleUrl = cloudinaryService.uploadAudio(maleAudio, "story_" + storyId + "_male.mp3");
        String femaleUrl = cloudinaryService.uploadAudio(femaleAudio, "story_" + storyId + "_female.mp3");

        AudioTrack maleTrack = audioTrackRepository.findByStoryAndVoiceType(story, VoiceTypeEnum.MALE);
        AudioTrack femaleTrack = audioTrackRepository.findByStoryAndVoiceType(story, VoiceTypeEnum.FEMALE);

        if (maleTrack != null) {
            maleTrack.setUrl(maleUrl);
            maleTrack.setTitle(story.getTitle() + " (Masculino)");
            audioTrackRepository.save(maleTrack);
        }
        if (femaleTrack != null) {
            femaleTrack.setUrl(femaleUrl);
            femaleTrack.setTitle(story.getTitle() + " (Femenino)");
            audioTrackRepository.save(femaleTrack);
        }
    }

    /**
     * Elimina las pistas de audio (masculina y femenina) asociadas a una historia.
     *
     * @param story historia para la que se eliminan las pistas
     */
    public void deleteAudioTracksForStory(Story story) {
        AudioTrack maleTrack = audioTrackRepository.findByStoryAndVoiceType(story, VoiceTypeEnum.MALE);
        AudioTrack femaleTrack = audioTrackRepository.findByStoryAndVoiceType(story, VoiceTypeEnum.FEMALE);

        if (maleTrack != null) {
            audioTrackRepository.delete(maleTrack);
        }
        if (femaleTrack != null) {
            audioTrackRepository.delete(femaleTrack);
        }
    }
}
