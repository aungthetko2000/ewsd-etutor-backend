package org.ewsd.service.assignment;

import lombok.RequiredArgsConstructor;
import org.ewsd.assignment.Assignment;
import org.ewsd.dto.assignment.AssignmentRequestDto;
import org.ewsd.dto.assignment.AssignmentResponseDto;
import org.ewsd.entity.tutor.Tutor;
import org.ewsd.entity.user.User;
import org.ewsd.repository.AssignmentRepository;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.service.file.FileStorageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final FileStorageService fileStorageService;

    @Override
    public AssignmentResponseDto createAssignment(AssignmentRequestDto request, MultipartFile file) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getTutor() == null) {
            throw new IllegalStateException("Only tutors can create assignments");
        }

        Tutor tutor = user.getTutor();

        if (request.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }

        fileStorageService.saveAssignment(file);

        Assignment assignment = new Assignment();
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDueDate(request.getDueDate());
        assignment.setCreatedAt(LocalDateTime.now());
        assignment.setUploadBy(tutor.getFullName());
        assignment.setSubject(request.getSubject());
        assignment.setTutor(tutor);

        Assignment saved = assignmentRepository.save(assignment);

        return mapToDto(saved);
    }

    @Override
    public List<AssignmentResponseDto> getAllAssignment() {
        return assignmentRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AssignmentResponseDto getAssignmentInfoById(Long id) {
        return assignmentRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new IllegalArgumentException("Assignment was not found"));
    }

    private AssignmentResponseDto mapToDto(Assignment assignment) {
        AssignmentResponseDto dto = new AssignmentResponseDto();
        dto.setId(assignment.getId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setUploadedBy(assignment.getUploadBy());
        dto.setSubject(assignment.getSubject());
        return dto;
    }
}
