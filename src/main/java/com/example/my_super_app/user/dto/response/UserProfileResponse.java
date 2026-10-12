package com.example.my_super_app.user.dto.response;

public record UserProfileResponse(
        Long userId,
        String displayName,
        String bio,
        String avatarUrl
) {}
