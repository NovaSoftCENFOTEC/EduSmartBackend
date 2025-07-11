package com.project.demo.logic.entity.story;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    Page<Story> findStoriesByCourseId(Long courseId, Pageable pageable);
}
