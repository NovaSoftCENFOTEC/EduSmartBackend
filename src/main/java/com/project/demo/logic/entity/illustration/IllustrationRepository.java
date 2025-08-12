package com.project.demo.logic.entity.illustration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Illustration.
 * Permite consultar ilustraciones por historia.
 */
@Repository
public interface IllustrationRepository extends JpaRepository<Illustration, Long> {
    /**
     * Busca las ilustraciones por el identificador de la historia.
     *
     * @param storyId  identificador de la historia
     * @param pageable paginación
     * @return página de ilustraciones
     */
    Page<Illustration> findIllustrationsByStoryId(Long storyId, Pageable pageable);
}
