package org.ewsd.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.upload.dir}")
    private String imageUrl;

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
}
