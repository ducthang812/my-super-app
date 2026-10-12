package com.example.my_super_app.user.service;

import com.example.my_super_app.user.dto.request.UpdateProfileRequest;
import com.example.my_super_app.user.dto.response.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(Long userId);
    UserProfileResponse updateProfile(Long userId, UpdateProfileRequest updateProfileRequest);
}
