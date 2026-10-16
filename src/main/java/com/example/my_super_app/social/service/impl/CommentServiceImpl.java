package com.example.my_super_app.social.service.impl;

import com.example.my_super_app.common.exception.ForbiddenActionException;
import com.example.my_super_app.common.exception.ResourceNotFoundException;
import com.example.my_super_app.social.dto.request.CreateCommentRequest;
import com.example.my_super_app.social.dto.response.CommentResponse;
import com.example.my_super_app.social.entity.Comment;
import com.example.my_super_app.social.repository.CommentRepository;
import com.example.my_super_app.social.repository.PostRepository;
import com.example.my_super_app.social.service.CommentService;
import com.example.my_super_app.social.service.CursorPage;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public CommentResponse createComment(Long postId, Long authorId, CreateCommentRequest request) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("POST_NOT_FOUND", "Post does not exist");
        }

        Comment comment = new Comment(postId, authorId, request.content());
        Comment saved = commentRepository.save(comment);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("COMMENT_NOT_FOUND", "Comment does not exist"));

        if (!comment.isOwnedBy(currentUserId)) {
            throw new ForbiddenActionException("FORBIDDEN_COMMENT_ACTION", "You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    @Override
    public CursorPage<CommentResponse> getCommentsByPost(Long postId, Long cursor, int limit) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("POST_NOT_FOUND", "Post does not exist");
        }

        List<Comment> comments = commentRepository.findByPostIdWithCursor(
                postId, cursor, PageRequest.of(0, limit + 1)
        );

        boolean hasMore = comments.size() > limit;
        List<Comment> pageItems = hasMore ? comments.subList(0, limit) : comments;

        Long nextCursor = pageItems.isEmpty() ? null : pageItems.get(pageItems.size() - 1).getId();

        List<CommentResponse> responses = pageItems.stream()
                .map(this::toResponse)
                .toList();

        return new CursorPage<>(responses, nextCursor, hasMore);
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getPostId(),
                comment.getAuthorId(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
