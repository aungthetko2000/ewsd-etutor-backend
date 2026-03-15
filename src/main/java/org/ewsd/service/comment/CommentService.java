package org.ewsd.service.comment;

import org.ewsd.dto.comment.CommentRequestDTO;
import org.ewsd.entity.comment.Comment;
import java.util.List;

public interface CommentService {
    Comment saveComment(CommentRequestDTO dto);
    List<Comment> getCommentsByBlog(Long blogId);
}