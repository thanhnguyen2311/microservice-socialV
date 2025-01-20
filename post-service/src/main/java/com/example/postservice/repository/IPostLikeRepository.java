package com.example.postservice.repository;

import com.example.postservice.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostLikeRepository extends JpaRepository<PostLike, Long> {
    Boolean existsByPostIdAndUserId(String postId, Long userId);
}
