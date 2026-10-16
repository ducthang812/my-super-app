package com.example.my_super_app.social.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "posts_like")
@IdClass(Like.LikeId.class)
public class Like {
    @Id
    private Long postId;
    @Id
    private Long userId;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Like(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public class LikeId implements Serializable {
        private Long postId;
        private Long userId;

        public LikeId() {
        }

        public LikeId(Long postId, Long userId) {
            this.postId = postId;
            this.userId = userId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(postId, userId);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof LikeId)) {
                return false;
            }
            return Objects.equals(userId, ((LikeId) obj).userId) && Objects.equals(postId, ((LikeId) obj).postId);
        }
    }
}


