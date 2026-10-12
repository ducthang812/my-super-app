package com.example.my_super_app.auth.dto.response;

public record AccountResponse(
        Long id,
        String username,
        String email
) {
}
