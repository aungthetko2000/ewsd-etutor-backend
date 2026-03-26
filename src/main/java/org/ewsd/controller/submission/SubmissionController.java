package org.ewsd.controller.submission;

import org.ewsd.entity.submission.Submission;
import org.ewsd.entity.user.User;
import org.ewsd.repository.submission.SubmissionRepository;
import org.ewsd.service.file.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/submissions")
public class SubmissionController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private SubmissionRepository submissionRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("assignmentId") Long assignmentId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        if (currentUser.getStudent() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only students can upload assignments.");
        }

        Long studentId = currentUser.getStudent().getId();

        String savedPath = fileStorageService.saveAssignment(file, assignmentId, studentId);

        Submission submission = new Submission();
        submission.setAssignmentId(assignmentId);
        submission.setStudentId(studentId);
        submission.setFilePath(savedPath);
        submission.setUploadTimestamp(LocalDateTime.now());

        submissionRepository.save(submission);

        return ResponseEntity.ok("Assignment uploaded successfully!");
    }

    @GetMapping("/download/{submissionId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long submissionId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        boolean isTutor = currentUser.getTutor() != null;
        boolean isOwner = currentUser.getStudent() != null &&
                submission.getStudentId().equals(currentUser.getStudent().getId());

        if (!isTutor && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied.");
        }

        try {
            Path path = Paths.get(submission.getFilePath());
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/my-submissions")
    public ResponseEntity<?> getMySubmissions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        if (currentUser.getStudent() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only students can view their submissions.");
        }

        return ResponseEntity.ok(submissionRepository.findByStudentId(currentUser.getStudent().getId()));
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<?> getAssignmentSubmissions(@PathVariable Long assignmentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        if (currentUser.getTutor() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only tutors can view all submissions for an assignment.");
        }

        return ResponseEntity.ok(submissionRepository.findByAssignmentId(assignmentId));
    }
}