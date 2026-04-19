package org.ewsd.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditCommentRequestDto {

    private Long commentId;
    private String description;

}
