package org.ewsd.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {

    private Long id;
    private String description;
    private String whoComment;
    private LocalDateTime timeStamp;
    private Long userId;
}
