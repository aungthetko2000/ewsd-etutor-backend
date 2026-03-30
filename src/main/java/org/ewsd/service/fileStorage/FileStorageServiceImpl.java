package org.ewsd.service.fileStorage;

import org.ewsd.service.fileStorage.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.upload.dir:/var/www/data/assignments}")
    private String uploadDir;

    private final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "doc", "docx", "ppt", "pptx", "xls", "xlsx", "pdf", "jpg", "jpeg", "png"
    );

    @Override
    public String saveAssignment(MultipartFile file, Long assignmentId, Long studentId) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(originalFilename);

            if (extension == null || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                throw new RuntimeException("File type not allowed.");
            }

            String newFileName = UUID.randomUUID().toString() + "." + extension.toLowerCase();

            Path targetDirectory = Paths.get(uploadDir)
                    .resolve(String.valueOf(assignmentId))
                    .resolve(String.valueOf(studentId))
                    .normalize();

            Files.createDirectories(targetDirectory);

            Path targetLocation = targetDirectory.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file securely: " + e.getMessage());
        }
    }
}