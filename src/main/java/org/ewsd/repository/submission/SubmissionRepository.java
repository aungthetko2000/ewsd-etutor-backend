package org.ewsd.repository.submission;

import org.ewsd.entity.submission.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    // List<Submission> findByStudentId(Long studentId);

    // List<Submission> findByAssignmentId(Long assignmentId);
}