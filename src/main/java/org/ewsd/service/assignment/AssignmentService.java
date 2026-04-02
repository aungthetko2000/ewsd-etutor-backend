package org.ewsd.service.assignment;

import org.ewsd.dto.assignment.AssignmentRequestDto;
import org.ewsd.dto.assignment.AssignmentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssignmentService {

    AssignmentResponseDto createAssignment(AssignmentRequestDto request, MultipartFile file);

    List<AssignmentResponseDto> getAllAssignment();

    AssignmentResponseDto getAssignmentInfoById(Long id);
}
