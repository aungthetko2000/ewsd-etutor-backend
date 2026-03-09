package org.ewsd.controller.blog;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ewsd.dto.blog.BlogCreateRequestDto;
import org.ewsd.dto.blog.BlogResponseDto;
import org.ewsd.dto.response.ApiResponse;
import org.ewsd.service.blog.BlogServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogServiceImpl blogService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BlogResponseDto>> createBlog(@Valid @ModelAttribute BlogCreateRequestDto blogCreateRequestDto, @RequestParam("image") MultipartFile file) {
        BlogResponseDto newBlog = blogService.createBlog(blogCreateRequestDto, file);
        ApiResponse<BlogResponseDto> response = ApiResponse.success(newBlog, "Blog created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_BLOG_LIST')")
    public ResponseEntity<ApiResponse<List<BlogResponseDto>>> getBlogs() {
        List<BlogResponseDto> list = blogService.getAllBlogs();
        ApiResponse<List<BlogResponseDto>> response = ApiResponse.success(list, "Get all blogs list");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_BLOG_LIST')")
    public ResponseEntity<ApiResponse<BlogResponseDto>> getBlogDetail(@PathVariable Long id) {
        BlogResponseDto blogResponseDto = blogService.getBlogById(id);
        ApiResponse<BlogResponseDto> response = ApiResponse.success(blogResponseDto, "Blog get successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/paginate")
    @PreAuthorize("hasAuthority('VIEW_BLOG_LIST')")
    public ResponseEntity<ApiResponse<List<BlogResponseDto>>> getMostFavoriteCountBlog() {
        List<BlogResponseDto> list = blogService.getMostFavoriteBlog();
        ApiResponse<List<BlogResponseDto>> response = ApiResponse.success(list, "Get all blogs list");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/favorite/{id}")
    @PreAuthorize("hasAuthority('LIKE_BLOG_POST')")
    public ResponseEntity<ApiResponse<Integer>> favoriteBlog(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        int favoriteCount = blogService.toggleFavorite(email, id);
        ApiResponse<Integer> response = ApiResponse.success(favoriteCount, "You liked this blog");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}