package org.ewsd.service.blog;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.blog.BlogCreateRequestDto;
import org.ewsd.dto.blog.BlogResponseDto;
import org.ewsd.entity.blog.Blog;
import org.ewsd.entity.user.User;
import org.ewsd.repository.blog.BlogRepository;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.service.file.FileStorageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Override
    public BlogResponseDto createBlog(BlogCreateRequestDto request, MultipartFile image) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        String imageUrl = fileStorageService.saveImage(image);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Blog blog = new Blog();
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setCreatedAt(LocalDateTime.now());
        blog.setImageUrl(imageUrl);
        blog.setUser(user);

        return mapToDto(blogRepository.save(blog));
    }

    @Override
    public List<BlogResponseDto> getAllBlogs() {
        return blogRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(blog -> BlogResponseDto.builder()
                        .id(blog.getId())
                        .title(blog.getTitle())
                        .content(blog.getContent())
                        .authorName(blog.getUser().getFirstName() + blog.getUser().getLastName())
                        .createdAt(blog.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public BlogResponseDto getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blog was not found"));
        return mapToDto(blog);
    }

    private BlogResponseDto mapToDto(Blog blog) {
        return BlogResponseDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .authorName(blog.getUser().getFirstName() + blog.getUser().getLastName())
                .createdAt(blog.getCreatedAt())
                .email(blog.getUser().getUsername())
                .build();
    }
}