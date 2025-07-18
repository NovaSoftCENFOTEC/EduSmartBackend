package com.project.demo.logic.entity.assignment;

import com.project.demo.logic.entity.group.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {


    Page<Assignment> findByGroupId(Long groupId, Pageable pageable);

    Page<Assignment> findByGroup(Group group, Pageable pageable);
}
