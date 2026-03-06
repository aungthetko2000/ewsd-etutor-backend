package org.ewsd.service.blog;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.blog.BlogCreateRequestDto;
import org.ewsd.dto.blog.BlogResponseDto;
import org.ewsd.entity.blog.Blog;
import org.ewsd.repository.blog.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public void createBlog(BlogCreateRequestDto request) {

        String fileName = saveImage(request.getImage());

        Blog blog = Blog.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .imageUrl(fileName)
                .createdAt(LocalDateTime.now())
                .build();

        blogRepository.save(blog);
    }

    public List<BlogResponseDto> getAllBlogs() {

        return blogRepository.findAll()
                .stream()
                .map(blog -> BlogResponseDto.builder()
                        .id(blog.getId())
                        .title(blog.getTitle())
                        .description(blog.getDescription())
                        .imageUrl(blog.getImageUrl())
                        .authorName(
                                blog.getCreatedBy() != null ?
                                        blog.getCreatedBy().getEmail() : "Unknown"
                        )
                        .build())
                .toList();
    }

    public BlogResponseDto getBlogById(Long id) {

        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        return BlogResponseDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .description(blog.getDescription())
                .imageUrl(blog.getImageUrl())
                .authorName(
                        blog.getCreatedBy() != null ?
                                blog.getCreatedBy().getEmail() : "Unknown"
                )
                .build();
    }

    private String saveImage(MultipartFile image) {

        String uploadDir = System.getProperty("user.home") + "/Documents/Blog/";

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        File file = new File(uploadDir + fileName);

        try {
            image.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image");
        }

        return fileName;
    }
}