package org.ewsd.controller.tutor;

import org.ewsd.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.dto.tutor.TutorResponse;
import org.ewsd.service.tutor.TutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tutors")
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    @GetMapping("/fetchAllTutors")
    @PreAuthorize("hasRole('STAFF') AND hasAuthority('BULK_ALLOCATION')")
    public ResponseEntity<ApiResponse<List<TutorResponse>>> fetchAllTutors() {
        List<TutorResponse> tutorResponse = tutorService.getAllTutors();
        ApiResponse<List<TutorResponse>> response = ApiResponse.success(tutorResponse,
                "Retrieve all tutors successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/assigned-students/{tutorId}")
    @PreAuthorize("hasRole('TUTOR') AND hasAuthority('VIEW_ASSIGNED_STUDENTS')")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAssignedStudents(
            @PathVariable Long tutorId) {

        List<StudentResponseDto> students = tutorService.getAssignedStudents(tutorId);

        ApiResponse<List<StudentResponseDto>> response =
                ApiResponse.success(students, "Assigned students retrieved successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}