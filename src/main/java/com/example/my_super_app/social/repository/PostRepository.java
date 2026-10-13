package com.example.my_super_app.social.repository;

import com.example.my_super_app.social.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
        SELECT p FROM Post p
        WHERE p.authorId = :authorId
        AND (:cursor IS NULL OR p.id < :cursor)
        ORDER BY p.id DESC
        """)
    List<Post> findByAuthorIdWithCursor(
            @Param("authorId") Long authorId,
            @Param("cursor") Long cursor,
            Pageable pageable
    );
}
