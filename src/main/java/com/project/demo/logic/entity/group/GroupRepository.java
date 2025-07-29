package com.project.demo.logic.entity.group;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Page<Group> findGroupsByCourseId(Long courseId, Pageable pageable);

    Page<Group> findGroupsByTeacherId(Long teacherId, Pageable pageable);

    Optional<Group> findWithStudentsById(Long groupId);

    @Query("SELECT DISTINCT g FROM Group g JOIN g.students s WHERE s.id = :studentId")
    Page<Group> findGroupsByStudentId(@Param("studentId") Long studentId, Pageable pageable);
}
