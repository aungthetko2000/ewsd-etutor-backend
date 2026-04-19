package org.ewsd.service.document;

import lombok.RequiredArgsConstructor;
import org.ewsd.assignment.Assignment;
import org.ewsd.dto.submission.SubmissionRequestDto;
import org.ewsd.dto.submission.SubmissionResponseDto;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.submission.Submission;
import org.ewsd.entity.user.User;
import org.ewsd.enumeration.SubmissionStatus;
import org.ewsd.repository.AssignmentRepository;
import org.ewsd.repository.submission.SubmissionRepository;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.service.file.FileStorageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;

    @Override
    public SubmissionResponseDto submitDocument(SubmissionRequestDto request, MultipartFile document) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getStudent() == null) {
            throw new IllegalStateException("Only students can submit assignments");
        }

        Student student = user.getStudent();

        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

        if (document == null || document.isEmpty()) {
            throw new IllegalArgumentException("Document file is required");
        }

        String fileName = fileStorageService.saveAssignment(document);

        Submission submission = new Submission();
        submission.setFileName(fileName);
        submission.setUploadTimestamp(LocalDateTime.now());
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setStatus(SubmissionStatus.PENDING);

        Submission savedSubmission = submissionRepository.save(submission);
        return mapToDto(savedSubmission);
    }

    @Override
    public List<SubmissionResponseDto> getAllDocumentsByStudentId(Long studentId, Long assignmentId) {
        User user = userRepository.findById(studentId).orElseThrow(() ->  new IllegalArgumentException("User was not found"));
        return submissionRepository.findByStudentIdAndAssignmentId(user.getStudent().getId(), assignmentId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubmissionResponseDto> getAllDocumentsId(Long studentId) {
        return submissionRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private SubmissionResponseDto mapToDto(Submission submission) {
        return SubmissionResponseDto.builder()
                .id(submission.getId())
                .fileName(submission.getFileName())
                .uploadTimestamp(submission.getUploadTimestamp())
                .assignmentId(submission.getAssignment().getId())
                .status(submission.getStatus().toString())
                .build();
    }
}
