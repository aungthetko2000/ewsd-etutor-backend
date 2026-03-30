package org.ewsd.service.fileStorage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveAssignment(MultipartFile file, Long assignmentId, Long studentId);
}