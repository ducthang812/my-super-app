package com.example.my_super_app.user.dto.request;

import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UpdateProfileRequest(

        @Size(max = 100)
        String displayName,

        @Size(max = 500)
        String bio,

        @URL
        @Size(max = 500)
        String avatarUrl
) {}
