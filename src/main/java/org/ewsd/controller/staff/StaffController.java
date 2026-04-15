package org.ewsd.controller.staff;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ewsd.dto.allocation.TutorAllocationRequest;
import org.ewsd.dto.allocation.TutorAllocationResponse;
import org.ewsd.dto.response.ApiResponse;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.service.staff.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping("/bulk-allocate")
    @PreAuthorize("hasRole('STAFF') AND hasAuthority('BULK_ALLOCATION')")
    public ResponseEntity<ApiResponse<List<TutorAllocationResponse>>> bulkAllocateStudents(@Valid @RequestBody TutorAllocationRequest request) {
        List<TutorAllocationResponse> allocationResponses = staffService.bulkAllocateStudentsToTutor(request.getStudentsId(), request.getTutorId());
        ApiResponse<List<TutorAllocationResponse>> response = ApiResponse.bulkSuccess(allocationResponses, "Students allocated successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/report/unassigned")
    @PreAuthorize("hasRole('STAFF') AND hasAuthority('VIEW_EXCEPTION_REPORT')")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAllStudentsWithNoTutor() {
        List<StudentResponseDto> allocationResponses = staffService.getStudentsWithNoTutor();
        ApiResponse<List<StudentResponseDto>> response = ApiResponse.bulkSuccess(allocationResponses, "Students retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/allocations")
    @PreAuthorize("hasRole('STAFF') AND hasAuthority('GET_ALLOCATION_LIST')")
    public ResponseEntity<ApiResponse<List<TutorAllocationResponse>>> getAllAllocations() {
        List<TutorAllocationResponse> allocationResponses = staffService.getAllAllocations();
        ApiResponse<List<TutorAllocationResponse>> response = ApiResponse.bulkSuccess(allocationResponses, "Students retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
