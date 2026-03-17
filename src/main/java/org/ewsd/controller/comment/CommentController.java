package org.ewsd.controller.comment;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.comment.CommentRequestDTO;
import org.ewsd.dto.response.ApiResponse;
import org.ewsd.entity.comment.Comment;
import org.ewsd.service.comment.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT') AND hasAuthority('POST_COMMENT')")
    public ResponseEntity<ApiResponse<Comment>> createComment(@RequestBody CommentRequestDTO dto) {
        Comment newComment = commentService.saveComment(dto);
        ApiResponse<Comment> response = ApiResponse.success(newComment, "Comment posted successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/blog/{blogId}")
    @PreAuthorize("hasRole('STUDENT') AND hasAuthority('VIEW_ALL_COMMENT')")
    public ResponseEntity<ApiResponse<List<Comment>>> getComments(@PathVariable Long blogId) {
        List<Comment> list = commentService.getCommentsByBlog(blogId);
        return ResponseEntity.ok(ApiResponse.success(list, "Retrieved successfully"));
    }
}