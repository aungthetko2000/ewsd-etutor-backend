package org.ewsd.service.document;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.blog.BlogResponseDto;
import org.ewsd.dto.submission.SubmissionRequestDto;
import org.ewsd.dto.submission.SubmissionResponseDto;
import org.ewsd.entity.blog.Blog;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.submission.Submission;
import org.ewsd.entity.user.User;
import org.ewsd.repository.submission.SubmissionRepository;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.service.file.FileStorageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

    @Override
    public SubmissionResponseDto submitDocument(SubmissionRequestDto request, MultipartFile document) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Student was not found"));

        String documentUrl = fileStorageService.saveAssignment(document);

        Submission submission = new Submission();
        submission.setFilePath(documentUrl);
        submission.setDescription(request.getDescription());
        submission.setDueDate(request.getDueDate());
        submission.setUploadTimestamp(LocalDateTime.now());
        submission.setStudent(user.getStudent());

        return mapToDto(submissionRepository.save(submission), documentUrl);
    }

    private SubmissionResponseDto mapToDto(Submission submission, String documentUrl) {

        return SubmissionResponseDto.builder()
                .filePath(documentUrl)
                .description(submission.getDescription())
                .uploadTimestamp(submission.getUploadTimestamp())
                .dueDate(submission.getDueDate())
                .build();
    }
}
