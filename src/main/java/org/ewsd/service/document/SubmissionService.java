package org.ewsd.service.document;

import org.ewsd.dto.submission.SubmissionRequestDto;
import org.ewsd.dto.submission.SubmissionResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface SubmissionService {

    SubmissionResponseDto submitDocument(SubmissionRequestDto request, MultipartFile document);

}
