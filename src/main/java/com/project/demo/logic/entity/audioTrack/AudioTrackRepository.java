package com.project.demo.logic.entity.audioTrack;

import com.project.demo.logic.entity.story.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad AudioTrack.
 * Proporciona métodos para consultar pistas de audio por historia y tipo de voz.
 */
@Repository
public interface AudioTrackRepository extends JpaRepository<AudioTrack, Long> {
    /**
     * Busca pistas de audio por el identificador de la historia.
     *
     * @param storyId  identificador de la historia
     * @param pageable paginación
     * @return página de pistas de audio
     */
    Page<AudioTrack> findAudioTracksByStoryId(Long storyId, Pageable pageable);

    /**
     * Busca una pista de audio por historia y tipo de voz.
     *
     * @param story         historia
     * @param voiceTypeEnum tipo de voz
     * @return pista de audio encontrada
     */
    AudioTrack findByStoryAndVoiceType(Story story, VoiceTypeEnum voiceTypeEnum);
}
