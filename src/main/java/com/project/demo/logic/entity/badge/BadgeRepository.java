package com.project.demo.logic.entity.badge;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BadgeRepository extends JpaRepository<Badge,Long> {
    @Query("SELECT b FROM Badge b JOIN b.students s WHERE s.id = :studentId")
    Page<Badge> findBadgesByStudentId(Long studentId, Pageable pageable);
}
