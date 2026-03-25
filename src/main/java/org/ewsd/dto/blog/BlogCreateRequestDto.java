package org.ewsd.dto.blog;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BlogCreateRequestDto {

    private String title;
    private String content;

}