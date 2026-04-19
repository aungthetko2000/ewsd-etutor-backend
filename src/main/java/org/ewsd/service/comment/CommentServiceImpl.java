package org.ewsd.service.comment;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.comment.CommentRequestDTO;
import org.ewsd.dto.comment.CommentResponseDto;
import org.ewsd.dto.comment.EditCommentRequestDto;
import org.ewsd.dto.student.StudentResponseDto;
import org.ewsd.entity.comment.Comment;
import org.ewsd.entity.blog.Blog;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.submission.Submission;
import org.ewsd.entity.user.User;
import org.ewsd.enumeration.NotificationType;
import org.ewsd.repository.comment.CommentRepository;
import org.ewsd.repository.blog.BlogRepository;
import org.ewsd.repository.submission.SubmissionRepository;
import org.ewsd.repository.user.UserRepository;
import org.ewsd.service.comment.CommentService;
import org.ewsd.service.notification.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public CommentResponseDto saveComment(CommentRequestDTO dto) {

        Blog blog = null;
        Submission submission = null;

        if ((dto.getBlogId() == null && dto.getSubmissionId() == null) ||
                (dto.getBlogId() != null && dto.getSubmissionId() != null)) {
            throw new IllegalArgumentException("Comment must belong to either blog OR submission");
        }

        if (dto.getBlogId() != null) {
            blog = blogRepository.findById(dto.getBlogId())
                    .orElseThrow(() -> new RuntimeException("Blog not found"));
        }

        if (dto.getSubmissionId() != null) {
            submission = submissionRepository.findById(dto.getSubmissionId())
                    .orElseThrow(() -> new RuntimeException("Submission not found"));
        }

        User user = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = Comment.builder()
                .description(dto.getDescription())
                .authorId(dto.getAuthorId())
                .createdAt(LocalDateTime.now())
                .user(user)
                .blog(blog)
                .submission(submission)
                .build();

        Comment savedComment = commentRepository.save(comment);

        if (submission != null) {
            Long ownerId = submission.getStudent().getUser().getId();
            Long commenterId = savedComment.getAuthorId();
            if (!ownerId.equals(commenterId)) {
                notificationService.sendAndSaveComment(
                        submission.getStudent().getUser(),
                        savedComment,
                        NotificationType.COMMENT,
                        "Someone commented on your submission"
                );
            }
        }

        return mapToDto(savedComment);
    }

    @Override
    public List<CommentResponseDto> getCommentsByBlog(Long blogId) {
        return commentRepository.findAllByBlogIdOrderByCreatedAtDesc(blogId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDto> getCommentsBySubmission(Long submissionId) {
        return commentRepository.findAllBySubmissionId(submissionId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CommentResponseDto mapToDto(Comment comment) {

        User user = userRepository.findById(comment.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("user was not found"));

        return CommentResponseDto.builder()
                .userId(user.getId())
                .id(comment.getId())
                .description(comment.getDescription())
                .whoComment(user.getFirstName() + ' ' +user.getLastName())
                .timeStamp(comment.getCreatedAt() != null ? comment.getCreatedAt() : null)
                .build();
    }

    @Override
    @Transactional
    public void updateComment(@RequestBody EditCommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(requestDto.getCommentId())
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + requestDto.getCommentId()));
        comment.setDescription(requestDto.getDescription());
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        commentRepository.delete(comment);
    }
}