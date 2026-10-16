package com.example.my_super_app.social.repository;

import com.example.my_super_app.social.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Like.LikeId> {
    public long countByPostId(long postId);
    public void deleteByPostIdAndUserId(long postId, long userId);
}
