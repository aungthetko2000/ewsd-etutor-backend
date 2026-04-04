package org.ewsd.repository.submission;

import org.ewsd.assignment.Assignment;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.submission.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

     List<Submission> findByStudentId(Long studentId);

    boolean existsByStudentAndAssignment(Student student, Assignment assignment);

}