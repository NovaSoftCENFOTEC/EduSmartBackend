package com.project.demo.logic.entity.material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Page<Material> findMaterialsByCourseId(Long courseId, Pageable pageable);

    Page<Material> findMaterialsByTeacherId(Long teacherId, Pageable pageable);
}
