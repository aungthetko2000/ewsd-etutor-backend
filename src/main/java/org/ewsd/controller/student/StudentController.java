package org.ewsd.controller.student;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.allocation.TutorAllocationResponse;
import org.ewsd.dto.response.ApiResponse;
import org.ewsd.dto.student.StudentRegisterRequest;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.dto.tutor.TutorResponse;
import org.ewsd.service.student.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    @PreAuthorize("(hasRole('STAFF') OR hasRole('AUTHORIZE_STAFF')) AND hasAuthority('BULK_ALLOCATION')")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAllStudents(@RequestParam(required = false) Boolean unassignedOnly) {
        List<StudentResponseDto> tutorResponse = studentService.getStudents(unassignedOnly);
        ApiResponse<List<StudentResponseDto>> response = ApiResponse.success(tutorResponse, "Retrieve all unassigned successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}