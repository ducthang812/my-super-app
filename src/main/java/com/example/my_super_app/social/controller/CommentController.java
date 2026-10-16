package com.example.my_super_app.social.controller;

import com.example.my_super_app.common.response.ApiResponse;
import com.example.my_super_app.common.security.CurrentUserId;
import com.example.my_super_app.social.dto.request.CreateCommentRequest;
import com.example.my_super_app.social.dto.response.CommentResponse;
import com.example.my_super_app.social.service.CommentService;
import com.example.my_super_app.social.service.CursorPage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @CurrentUserId Long authorId,
            @Valid @RequestBody CreateCommentRequest request) {

        CommentResponse response = commentService.createComment(postId, authorId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/posts/{postId}/comments")
    public ApiResponse<List<CommentResponse>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) Long cursor) {

        CursorPage<CommentResponse> page = commentService.getCommentsByPost(postId, cursor, limit);

        return ApiResponse.success(
                page.items(),
                new ApiResponse.Pagination(
                        page.nextCursor() != null ? String.valueOf(page.nextCursor()) : null,
                        page.hasMore(),
                        limit
                )
        );
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @CurrentUserId Long currentUserId) {

        commentService.deleteComment(commentId, currentUserId);
        return ResponseEntity.noContent().build();
    }
}
