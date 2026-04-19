package org.ewsd.controller.comment;

import lombok.RequiredArgsConstructor;
import org.ewsd.dto.comment.CommentRequestDTO;
import org.ewsd.dto.comment.CommentResponseDto;
import org.ewsd.dto.comment.EditCommentRequestDto;
import org.ewsd.dto.response.ApiResponse;
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
    @PreAuthorize("(hasRole('STUDENT') OR hasRole('TUTOR')) AND hasAuthority('POST_COMMENT')")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(@RequestBody CommentRequestDTO dto) {
        CommentResponseDto newComment = commentService.saveComment(dto);
        ApiResponse<CommentResponseDto> response = ApiResponse.success(newComment, "Comment posted successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{blogId}")
    @PreAuthorize("(hasRole('STUDENT') OR hasRole('TUTOR')) AND hasAuthority('VIEW_ALL_COMMENT')")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getComments(@PathVariable Long blogId) {
        List<CommentResponseDto> list = commentService.getCommentsByBlog(blogId);
        ApiResponse<List<CommentResponseDto>> response = ApiResponse.success(list, "Get all comments by blog");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/submission/{submissionId}")
    @PreAuthorize("(hasRole('STUDENT') OR hasRole('TUTOR')) AND hasAuthority('VIEW_ALL_FEEDBACKS')")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getFeedBacks(@PathVariable Long submissionId) {
        List<CommentResponseDto> list = commentService.getCommentsBySubmission(submissionId);
        ApiResponse<List<CommentResponseDto>> response = ApiResponse.success(list, "Get all comments by blog");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("(hasRole('STUDENT') OR hasRole('TUTOR')) AND hasAuthority('EDIT_COMMENT')")
    public ResponseEntity<ApiResponse<String>> updateComment(@RequestBody EditCommentRequestDto requestDto) {
        commentService.updateComment(requestDto);
        return ResponseEntity.ok(ApiResponse.success(null, "Comment updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("(hasRole('STUDENT') OR hasRole('TUTOR')) AND hasAuthority('DELETE_COMMENT')")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Comment deleted successfully"));
    }
}