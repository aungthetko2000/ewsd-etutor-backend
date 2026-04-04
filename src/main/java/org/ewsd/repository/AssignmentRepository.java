package org.ewsd.repository;

import org.ewsd.assignment.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByTutor_Id(Long tutorId);
}
