package com.example.postservice.service;

import com.example.postservice.dto.UserInfo;
import com.example.postservice.entity.CommentLike;

import java.util.List;

public interface ICommentLikeService {
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);
    CommentLike save(CommentLike commentLike);
    List<UserInfo> findByCommentId(Long commentId);
}
