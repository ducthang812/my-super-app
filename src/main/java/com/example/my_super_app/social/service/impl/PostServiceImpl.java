package com.example.my_super_app.social.service.impl;

import com.example.my_super_app.common.exception.ForbiddenActionException;
import com.example.my_super_app.common.exception.ResourceNotFoundException;
import com.example.my_super_app.social.dto.request.CreatePostRequest;
import com.example.my_super_app.social.dto.response.PostResponse;
import com.example.my_super_app.social.entity.Post;
import com.example.my_super_app.social.repository.PostRepository;
import com.example.my_super_app.social.service.CursorPage;
import com.example.my_super_app.social.service.PostService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostResponse createPost(Long authorId, CreatePostRequest request) {
        Post post = new Post(authorId, request.content());
        Post savedPost  = postRepository.save(post);
        return toResponse(savedPost);
    }

    @Override
    public PostResponse getPost(Long postId) {
        Post post = findByIdOrThrow(postId);
        return toResponse(post);
    }

    @Transactional
    @Override
    public void deletePost(Long postId, Long currentUserId) {
        Post post = findByIdOrThrow(postId);
        if (!post.isOwnedBy(currentUserId)) {
            throw new ForbiddenActionException("FORBIDDEN_POST_ACTION", "You can only delete your own posts");
        }
        postRepository.delete(post);
    }

    @Override
    public CursorPage<PostResponse> getPostsByAuthor(Long authorId, Long cursor, int limit) {
        List<Post> posts = postRepository.findByAuthorIdWithCursor(authorId, cursor, PageRequest.of(0, limit + 1));
        boolean hasMore = posts.size() > limit;
        List<Post> pageItems = hasMore ? posts.subList(0, limit) : posts;
        Long nextCursor = pageItems.isEmpty() ? null : pageItems.get(pageItems.size() - 1).getId();
        List<PostResponse> postResponses =  pageItems.stream().map(this::toResponse).toList();
        return new CursorPage<>(postResponses, nextCursor, hasMore);
    }

    private Post findByIdOrThrow(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("POST_NOT_FOUND", "Post does not exist"));
    }

    private PostResponse toResponse(Post post) {
        return new PostResponse(post.getId(), post.getAuthorId(), post.getContent(), post.getCreatedAt());
    }
}
