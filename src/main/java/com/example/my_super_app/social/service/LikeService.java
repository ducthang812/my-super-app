package com.example.my_super_app.social.service;

public interface LikeService {
    boolean like(Long postId, Long userId);
    boolean unlike(Long postId, Long userId);
    long countLikes(Long postId);
}
