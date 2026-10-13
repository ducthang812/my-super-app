package com.example.my_super_app.social.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(

        @NotBlank
        @Size(max = 2000)
        String content
) {}
