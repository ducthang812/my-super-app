package com.example.my_super_app.user.service.impl;

import com.example.my_super_app.common.exception.ResourceNotFoundException;
import com.example.my_super_app.user.dto.request.UpdateProfileRequest;
import com.example.my_super_app.user.dto.response.UserProfileResponse;
import com.example.my_super_app.user.entity.UserProfile;
import com.example.my_super_app.user.repository.UserProfileRepository;
import com.example.my_super_app.user.service.UserProfileQueryService;
import com.example.my_super_app.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService, UserProfileQueryService {
    private final UserProfileRepository userProfileRepository;

    public UserServiceImpl(UserProfileRepository userRepository) {
        this.userProfileRepository = userRepository;
    }

    @Override
    @Transactional
    public void createEmptyProfile(Long userId) {
        UserProfile userProfile = new UserProfile(userId);
        userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfileResponse getProfile(Long userId) {
        UserProfile userProfile = findByIdOrThrow(userId);
        return toResponse(userProfile);
    }

    @Override
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest updateProfileRequest) {
        UserProfile userProfile = findByIdOrThrow(userId);
        userProfile.updateProfile(updateProfileRequest.displayName(), updateProfileRequest.bio(), userProfile.getAvatarUrl());
        return toResponse(userProfile);
    }

    private UserProfile findByIdOrThrow(Long userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User does not exist"));
    }

    private UserProfileResponse toResponse(UserProfile profile) {
        return new UserProfileResponse(
                profile.getUserId(),
                profile.getDisplayName(),
                profile.getBio(),
                profile.getAvatarUrl()
        );
    }


}
