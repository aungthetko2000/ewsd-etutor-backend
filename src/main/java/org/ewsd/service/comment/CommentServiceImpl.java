package org.ewsd.service.comment;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.comment.CommentRequestDTO;
import org.ewsd.dto.comment.CommentResponseDto;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.entity.comment.Comment;
import org.ewsd.entity.blog.Blog;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.user.User;
import org.ewsd.repository.comment.CommentRepository;
import org.ewsd.repository.blog.BlogRepository;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.service.comment.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentResponseDto saveComment(CommentRequestDTO dto) {
        Blog blog = blogRepository.findById(dto.getBlogId())
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        User user = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("User was not found"));

        Comment comment = Comment.builder()
                .description(dto.getDescription())
                .authorId(dto.getAuthorId())
                .createdAt(LocalDateTime.now())
                .user(user)
                .blog(blog)
                .build();

        return mapToDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponseDto> getCommentsByBlog(Long blogId) {
        return commentRepository.findAllByBlogIdOrderByCreatedAtDesc(blogId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CommentResponseDto mapToDto(Comment comment) {

        User user = userRepository.findById(comment.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("user was not found"));

        return CommentResponseDto.builder()
                .id(comment.getId())
                .description(comment.getDescription())
                .whoComment(user.getFirstName() + ' ' +user.getLastName())
                .timeStamp(comment.getCreatedAt() != null ? comment.getCreatedAt() : null)
                .build();
    }

    @Override
    @Transactional
    public void updateComment(Long id, CommentRequestDTO dto, Long currentUserId, boolean isAdmin) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        if (!comment.getAuthorId().equals(currentUserId) && !isAdmin) {
            throw new RuntimeException("Unauthorized: You cannot edit this comment.");
        }

        comment.setDescription(dto.getDescription());
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id, Long currentUserId, boolean isAdmin) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        if (!comment.getAuthorId().equals(currentUserId) && !isAdmin) {
            throw new RuntimeException("Unauthorized: You cannot delete this comment.");
        }

        commentRepository.delete(comment);
    }
}