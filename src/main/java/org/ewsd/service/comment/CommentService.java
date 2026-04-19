package org.ewsd.service.comment;

import org.ewsd.dto.comment.CommentRequestDTO;
import org.ewsd.dto.comment.CommentResponseDto;
import org.ewsd.dto.comment.EditCommentRequestDto;
import org.ewsd.entity.comment.Comment;
import java.util.List;

public interface CommentService {

    CommentResponseDto saveComment(CommentRequestDTO dto);
    
    List<CommentResponseDto> getCommentsByBlog(Long blogId);

    List<CommentResponseDto> getCommentsBySubmission(Long submissionId);

    void updateComment(EditCommentRequestDto editCommentRequestDto);

    void deleteComment(Long id);
}