package com.project.demo.logic.entity.audioTrack;

import com.project.demo.logic.entity.story.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioTrackRepository extends JpaRepository<AudioTrack, Long> {
    Page<AudioTrack> findAudioTracksByStoryId(Long storyId, Pageable pageable);

    AudioTrack findByStoryAndVoiceType(Story story, VoiceTypeEnum voiceTypeEnum);
}
