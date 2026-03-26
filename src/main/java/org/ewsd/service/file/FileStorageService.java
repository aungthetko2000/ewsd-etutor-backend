package org.ewsd.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveImage(MultipartFile file);

    String saveAssignment(MultipartFile file, Long assignmentId, Long studentId);
}
