package org.ewsd.dto.blog;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BlogResponseDto {

    private Long id;
    private String title;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
    private String email;
    private String imageUrl;
    private int favoriteCount;
    private boolean likedByCurrentUser;
}