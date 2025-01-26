package com.example.postservice.repository;

import com.example.postservice.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Boolean existsByCommentIdAndUserId(Long commentId, Long userId);
}
