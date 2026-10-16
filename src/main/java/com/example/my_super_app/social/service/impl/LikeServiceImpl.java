package com.example.my_super_app.social.service.impl;

import com.example.my_super_app.common.exception.ResourceNotFoundException;
import com.example.my_super_app.social.entity.Like;
import com.example.my_super_app.social.repository.LikeRepository;
import com.example.my_super_app.social.repository.PostRepository;
import com.example.my_super_app.social.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public boolean like(Long postId, Long userId) {
        if (postRepository.findById(postId).isEmpty()) {
            throw new ResourceNotFoundException("POST_NOT_FOUND", "Post does not exist");
        }
        try {
            likeRepository.save(new Like(postId, userId));
        } catch (DataIntegrityViolationException ignored) {
        }
        return true;
    }

    @Override
    @Transactional
    public boolean unlike(Long postId, Long userId) {
        if (postRepository.findById(postId).isEmpty()) {
            throw new ResourceNotFoundException("POST_NOT_FOUND", "Post does not exist");
        }
        likeRepository.deleteByPostIdAndUserId(postId, userId);
        return false;
    }

    @Override
    public long countLikes(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
