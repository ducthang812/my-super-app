package com.example.my_super_app.social.service;

import com.example.my_super_app.social.dto.request.CreatePostRequest;
import com.example.my_super_app.social.dto.response.PostResponse;

public interface PostService {
    PostResponse createPost(Long authorId, CreatePostRequest request);
    PostResponse getPost(Long postId);
    void deletePost(Long postId, Long currentUserId);
    CursorPage<PostResponse> getPostsByAuthor(Long authorId, Long cursor, int limit);
}
