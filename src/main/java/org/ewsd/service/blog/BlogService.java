package org.ewsd.service.blog;

import org.ewsd.dto.blog.BlogCreateRequestDto;
import org.ewsd.dto.blog.BlogResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BlogService {

    BlogResponseDto createBlog(BlogCreateRequestDto request, MultipartFile image);

    List<BlogResponseDto> getAllBlogs();

    BlogResponseDto getBlogById(Long id);

}
