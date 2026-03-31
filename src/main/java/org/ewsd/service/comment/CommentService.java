package org.ewsd.service.comment;

import org.ewsd.dto.comment.CommentRequestDTO;
import org.ewsd.dto.comment.CommentResponseDto;
import org.ewsd.entity.comment.Comment;
import java.util.List;

public interface CommentService {

    CommentResponseDto saveComment(CommentRequestDTO dto);
    
    List<CommentResponseDto> getCommentsByBlog(Long blogId);

    void updateComment(Long id, CommentRequestDTO dto, Long currentUserId, boolean isAdmin);

    void deleteComment(Long id, Long currentUserId, boolean isAdmin);
}