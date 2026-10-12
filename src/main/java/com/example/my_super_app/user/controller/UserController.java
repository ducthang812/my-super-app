package com.example.my_super_app.user.controller;

import com.example.my_super_app.common.response.ApiResponse;
import com.example.my_super_app.user.dto.request.UpdateProfileRequest;
import com.example.my_super_app.user.dto.response.UserProfileResponse;
import com.example.my_super_app.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/id")
    public ApiResponse<UserProfileResponse> getProfile(@PathVariable Long id) {
        return ApiResponse.success(userService.getProfile(id));
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getMyProfile() {
        return ApiResponse.success(userService.getProfile(currentUserId()));
    }

    public ApiResponse<UserProfileResponse> updateMyProfile(@Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        return ApiResponse.success(userService.updateProfile(currentUserId(), updateProfileRequest));
    }

    private Long currentUserId() {
        return (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
