package com.example.my_super_app.social.dto.response;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        Long authorId,
        String content,
        LocalDateTime createdAt
) {}
