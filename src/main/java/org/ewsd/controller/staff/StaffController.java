package org.ewsd.controller.staff;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ewsd.dto.allocation.TutorAllocationRequest;
import org.ewsd.dto.allocation.TutorAllocationResponse;
import org.ewsd.dto.report.MessageLastDaysDto;
import org.ewsd.dto.report.TutorMessageAverageResponse;
import org.ewsd.dto.response.ApiResponse;
import org.ewsd.dto.student.StudentRegisterRequest;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.service.report.StatisticsService;
import org.ewsd.service.staff.StaffService;
import org.ewsd.service.student.StudentService;
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
    private final StatisticsService statisticsService;
    private final StudentService studentService;

    @PostMapping("/bulk-allocate")
    @PreAuthorize("(hasRole('STAFF') or hasRole('AUTHORIZE_STAFF')) and hasAuthority('BULK_ALLOCATION')")
    public ResponseEntity<ApiResponse<List<TutorAllocationResponse>>> bulkAllocateStudents(@Valid @RequestBody TutorAllocationRequest request) {
        List<TutorAllocationResponse> allocationResponses = staffService.bulkAllocateStudentsToTutor(request.getStudentsId(), request.getTutorId());
        ApiResponse<List<TutorAllocationResponse>> response = ApiResponse.bulkSuccess(allocationResponses, "Students allocated successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/report/unassigned")
    @PreAuthorize("(hasRole('STAFF') or hasRole('AUTHORIZE_STAFF')) AND hasAuthority('VIEW_EXCEPTION_REPORT')")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAllStudentsWithNoTutor() {
        List<StudentResponseDto> allocationResponses = staffService.getStudentsWithNoTutor();
        ApiResponse<List<StudentResponseDto>> response = ApiResponse.bulkSuccess(allocationResponses, "Students retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/allocations")
    @PreAuthorize("(hasRole('STAFF') or hasRole('AUTHORIZE_STAFF'))AND hasAuthority('GET_ALLOCATION_LIST')")
    public ResponseEntity<ApiResponse<List<TutorAllocationResponse>>> getAllAllocations() {
        List<TutorAllocationResponse> allocationResponses = staffService.getAllAllocations();
        ApiResponse<List<TutorAllocationResponse>> response = ApiResponse.bulkSuccess(allocationResponses, "Students retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/students/inactive")
    @PreAuthorize("(hasRole('STAFF') or hasRole('AUTHORIZE_STAFF')) AND hasAuthority('VIEW_EXCEPTION_REPORT')")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getInactiveStudents(@RequestParam("days") Integer days) {
        List<StudentResponseDto> allocationResponses = staffService.getInactiveStudentsReport(days);
        ApiResponse<List<StudentResponseDto>> response = ApiResponse.bulkSuccess(allocationResponses, "Inactive students retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tutor-message-average")
    @PreAuthorize("(hasRole('STAFF') or hasRole('AUTHORIZE_STAFF')) AND hasAuthority('VIEW_STATISTICS_REPORT')")
    public ResponseEntity<ApiResponse<List<TutorMessageAverageResponse>>> getInactiveStudents() {
        List<TutorMessageAverageResponse> reportResponse = statisticsService.getTutorMessageAverages();
        ApiResponse<List<TutorMessageAverageResponse>> response = ApiResponse.bulkSuccess(reportResponse, "Report retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/messages-last-7-days")
    @PreAuthorize("(hasRole('STAFF') or hasRole('AUTHORIZE_STAFF')) AND hasAuthority('VIEW_STATISTICS_REPORT')")
    public ResponseEntity<ApiResponse<List<MessageLastDaysDto>>> getMessagesLast7Days() {
        List<MessageLastDaysDto> reportResponse = statisticsService.getMessagesLast7Days();
        ApiResponse<List<MessageLastDaysDto>> response = ApiResponse.bulkSuccess(reportResponse, "Report retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('STAFF') AND hasAuthority('CREATE_STUDENT')")
    public ResponseEntity<ApiResponse<StudentResponseDto>> registerStudent(@RequestBody StudentRegisterRequest request) {
        StudentResponseDto response = studentService.registerStudent(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Student created successfully"));
    }
}
