package com.example.my_super_app.social.service;

import com.example.my_super_app.social.dto.request.CreateCommentRequest;
import com.example.my_super_app.social.dto.response.CommentResponse;

public interface CommentService {
    CommentResponse createComment(Long postId, Long authorId, CreateCommentRequest request);
    void deleteComment(Long commentId, Long currentUserId);
    CursorPage<CommentResponse> getCommentsByPost(Long postId, Long cursor, int limit);
}
