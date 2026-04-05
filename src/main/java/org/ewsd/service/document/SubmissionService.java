package org.ewsd.service.document;

import org.ewsd.dto.submission.SubmissionRequestDto;
import org.ewsd.dto.submission.SubmissionResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SubmissionService {

    SubmissionResponseDto submitDocument(SubmissionRequestDto request, MultipartFile document);

    List<SubmissionResponseDto> getAllDocumentsByStudentId(Long studentId, Long assignmentId);

    List<SubmissionResponseDto> getAllDocumentsId(Long studentId);
}
