package com.example.my_super_app.social.controller;

import com.example.my_super_app.common.response.ApiResponse;
import com.example.my_super_app.common.security.CurrentUserId;
import com.example.my_super_app.social.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LikeController {
    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/posts/{postId}/likes")
    public ApiResponse<Map<String, Boolean>> like (@CurrentUserId Long userId, @PathVariable Long postId) {
        boolean liked = likeService.like(userId, postId);
        return null;
    }

}
