package org.ewsd.controller.blog;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.blog.BlogCreateRequestDto;
import org.ewsd.dto.blog.BlogResponseDto;
import org.ewsd.service.blog.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // 1️⃣ Upload blog
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_BLOG')")
    public ResponseEntity<String> createBlog(@ModelAttribute BlogCreateRequestDto request) {
        blogService.createBlog(request);
        return ResponseEntity.ok("Blog created successfully");
    }

    // 2️⃣ Get all blogs
    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_BLOG_LIST')")
    public List<BlogResponseDto> getBlogs() {
        return blogService.getAllBlogs();
    }

    // 3️⃣ Get blog detail
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_BLOG_LIST')")
    public BlogResponseDto getBlogDetail(@PathVariable Long id) {
        return blogService.getBlogById(id);
    }
}