package com.example.my_super_app.social.controller;

import com.example.my_super_app.common.response.ApiResponse;
import com.example.my_super_app.common.security.CurrentUserId;
import com.example.my_super_app.social.dto.request.CreatePostRequest;
import com.example.my_super_app.social.dto.response.PostResponse;
import com.example.my_super_app.social.service.CursorPage;
import com.example.my_super_app.social.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@CurrentUserId Long userId , @Valid @RequestBody CreatePostRequest createPostRequest) {
        PostResponse postResponse = postService.createPost(userId, createPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(postResponse));
    }

    @GetMapping("/posts/{id}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId) {
        return ApiResponse.success(postService.getPost(postId));
    }

    @GetMapping("/users/{id}/posts")
    public ApiResponse<List<PostResponse>> getPostByAuthor(@PathVariable Long authorId, @RequestParam(defaultValue = "20") int limit, @RequestParam(required = false) Long cursor) {
        CursorPage<PostResponse> pages = postService.getPostsByAuthor(authorId, cursor, limit);
        return ApiResponse.success(pages.items(),
                new ApiResponse.Pagination(
                        pages.nextCursor() != null ? String.valueOf(pages.nextCursor()) : null, pages.hasMore(), limit));
    }
}
