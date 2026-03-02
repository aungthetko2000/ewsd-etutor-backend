package org.ewsd.dto.blog;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogResponseDto {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String authorName;
}