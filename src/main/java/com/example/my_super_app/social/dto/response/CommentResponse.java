package com.example.my_super_app.social.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long postId,
        Long authorId,
        String content,
        LocalDateTime createdAt
) {}
