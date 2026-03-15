package org.ewsd.service.comment;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.comment.CommentRequestDTO;
import org.ewsd.entity.comment.Comment;
import org.ewsd.entity.blog.Blog;
import org.ewsd.repository.comment.CommentRepository;
import org.ewsd.repository.blog.BlogRepository;
import org.ewsd.service.comment.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;

    @Override
    @Transactional
    public Comment saveComment(CommentRequestDTO dto) {
        Blog blog = blogRepository.findById(dto.getBlogId())
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .authorId(dto.getAuthorId())
                .blog(blog)
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByBlog(Long blogId) {
        return commentRepository.findAllByBlogIdOrderByCreatedAtDesc(blogId);
    }
}