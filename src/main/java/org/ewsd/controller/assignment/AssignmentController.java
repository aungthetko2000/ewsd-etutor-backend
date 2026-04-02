package org.ewsd.controller.assignment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ewsd.dto.assignment.AssignmentRequestDto;
import org.ewsd.dto.assignment.AssignmentResponseDto;
import org.ewsd.dto.response.ApiResponse;
import org.ewsd.service.assignment.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('SUBMIT_ASSIGNMENT')")
    public ResponseEntity<ApiResponse<AssignmentResponseDto>> createBlog(@Valid @ModelAttribute AssignmentRequestDto assignmentRequestDto, @RequestParam("assignment") MultipartFile file) {
        AssignmentResponseDto newAssignment = assignmentService.createAssignment(assignmentRequestDto, file);
        ApiResponse<AssignmentResponseDto> response = ApiResponse.success(newAssignment, "Assignment created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_ALL_ASSIGNMENT')")
    public ResponseEntity<ApiResponse<List<AssignmentResponseDto>>> getAllAssignment() {
        List<AssignmentResponseDto> assignmentList = assignmentService.getAllAssignment();
        ApiResponse<List<AssignmentResponseDto>> response = ApiResponse.success(assignmentList, "Get all assignment");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_ASSIGNMENT')")
    public ResponseEntity<ApiResponse<AssignmentResponseDto>> getAllAssignment(@PathVariable("id") Long id) {
        AssignmentResponseDto assignmentList = assignmentService.getAssignmentInfoById(id);
        ApiResponse<AssignmentResponseDto> response = ApiResponse.success(assignmentList, "Get assignment");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
