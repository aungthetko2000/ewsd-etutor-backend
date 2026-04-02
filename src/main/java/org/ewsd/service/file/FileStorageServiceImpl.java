package org.ewsd.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.upload.image}")
    private String imageUrl;

    @Value("${app.upload.document}")
    private String documentUrl;

    private final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "doc", "docx", "ppt", "pptx", "xls", "xlsx", "pdf", "jpg", "jpeg", "png"
    );

    @Override
    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            Path dirPath = Paths.get(imageUrl);
            Files.createDirectories(dirPath);

            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = UUID.randomUUID() + extension;

            Path filePath = dirPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            return fileName;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to store image", exception);
        }
    }

    @Override
    public String saveAssignment(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(originalFilename);

            if (extension == null || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                throw new RuntimeException("File type not allowed.");
            }

            String newFileName = UUID.randomUUID().toString() + "." + extension.toLowerCase();

            Path targetDirectory = Paths.get(documentUrl)
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
