package org.ewsd.controller.submission;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ewsd.dto.response.ApiResponse;
import org.ewsd.dto.submission.SubmissionRequestDto;
import org.ewsd.dto.submission.SubmissionResponseDto;
import org.ewsd.service.document.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SUBMIT_DOCUMENT')")
    public ResponseEntity<ApiResponse<SubmissionResponseDto>> submitDocument(@Valid @ModelAttribute SubmissionRequestDto requestDto, @RequestParam("file") MultipartFile file) {
        SubmissionResponseDto newBlog = submissionService.submitDocument(requestDto, file);
        ApiResponse<SubmissionResponseDto> response = ApiResponse.success(newBlog, "Document Submitted");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}