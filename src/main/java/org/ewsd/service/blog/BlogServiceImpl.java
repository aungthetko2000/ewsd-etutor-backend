package org.ewsd.service.blog;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.ewsd.dto.blog.BlogCreateRequestDto;
import org.ewsd.dto.blog.BlogResponseDto;
import org.ewsd.entity.blog.Blog;
import org.ewsd.entity.blog.BlogFavorite;
import org.ewsd.entity.user.User;
import org.ewsd.repository.blog.BlogRepository;
import org.ewsd.repository.blogfavorite.BlogFavoriteRepository;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.service.file.FileStorageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final BlogFavoriteRepository blogFavoriteRepository;

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

        return mapToDto(blogRepository.save(blog), email);
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
                        .imageUrl(blog.getImageUrl())
                        .createdAt(blog.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public BlogResponseDto getBlogById(Long id) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blog was not found"));
        return mapToDto(blog, email);
    }

    @Transactional
    public int toggleFavorite(String userEmail, Long blogId) {

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found"));

        Optional<BlogFavorite> existing =
                blogFavoriteRepository.findByUserEmailAndBlogId(userEmail, blogId);

        if (existing.isPresent()) {
            blogFavoriteRepository.delete(existing.get()); // unlike
        } else {
            BlogFavorite favorite = new BlogFavorite();
            favorite.setBlog(blog);
            favorite.setUser(userRepository.findByEmail(userEmail).orElseThrow());
            blogFavoriteRepository.save(favorite); // like
        }

        int newCount = blogFavoriteRepository.countByBlogId(blogId);
        blog.setFavoriteCount(newCount);

        return newCount;
    }

    @Override
    public List<BlogResponseDto> getMostFavoriteBlog() {
        return blogRepository.findTop6ByOrderByFavoriteCountDesc()
                .stream()
                .map(blog -> BlogResponseDto.builder()
                        .id(blog.getId())
                        .title(blog.getTitle())
                        .content(blog.getContent())
                        .authorName(blog.getUser().getFirstName() + blog.getUser().getLastName())
                        .favoriteCount(blog.getFavoriteCount())
                        .createdAt(blog.getCreatedAt())
                        .imageUrl(blog.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    private BlogResponseDto mapToDto(Blog blog, String userEmail) {

        boolean liked = blogFavoriteRepository.existsByUserEmailAndBlogId(userEmail, blog.getId());

        return BlogResponseDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .authorName(blog.getUser().getFirstName() + blog.getUser().getLastName())
                .createdAt(blog.getCreatedAt())
                .email(blog.getUser().getUsername())
                .imageUrl(blog.getImageUrl())
                .favoriteCount(blog.getFavoriteCount())
                .likedByCurrentUser(liked)
                .build();
    }

    @Override
    public List<BlogResponseDto> searchBlogs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBlogs();
        }

        String sanitizedKeyword = keyword.trim();
        if (sanitizedKeyword.length() < 3) {
            return List.of();
        }

        return blogRepository.searchBlogs(sanitizedKeyword)
                .stream()
                .map(blog -> BlogResponseDto.builder()
                        .id(blog.getId())
                        .title(blog.getTitle())
                        .content(blog.getContent())
                        .authorName(blog.getUser().getFirstName() + " " + blog.getUser().getLastName())
                        .imageUrl(blog.getImageUrl())
                        .favoriteCount(blog.getFavoriteCount())
                        .createdAt(blog.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}