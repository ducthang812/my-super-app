package com.example.my_super_app.user.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles", schema = "my_super_app")
public class UserProfile {
    @Id
    private Long userId;

    @Column(name = "display_name", length = 100)
    private String displayName;
    @Column(length = 500)
    private String bio;
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserProfile() {
    }

    public UserProfile(Long userId) {
        this.userId = userId;
    }

    public void updateProfile(String displayName, String bio, String avatarUrl) {
        this.displayName = displayName;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
