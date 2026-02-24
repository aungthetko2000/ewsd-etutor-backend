package org.ewsd.controller.student;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.service.student.StudentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/bulk-allocate")
    @PreAuthorize("hasRole('STAFF') and hasAuthority('BULK_ALLOCATION')")
    public List<StudentResponseDto> getStudents(
            @RequestParam(required = false) Boolean unassignedOnly
    ) {
        return studentService.getStudents(unassignedOnly);
    }
}