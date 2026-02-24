package org.ewsd.controller.tutor;

import org.ewsd.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.ewsd.dto.tutor.TutorResponse;
import org.ewsd.service.tutor.TutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tutors")
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    @GetMapping("/fetchAllTutors")
    //  @PreAuthorize("hasRole('STAFF') AND hasAuthority('BULK_ALLOCATION')")
    public ResponseEntity<ApiResponse<List<TutorResponse>>> fetchAllTutors() {
        List<TutorResponse> tutorResponse = tutorService.getAllTutors();
        ApiResponse<List<TutorResponse>> response = ApiResponse.success(tutorResponse,
                "Retrieve all tutors successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}