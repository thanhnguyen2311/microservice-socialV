package com.example.postservice.service;

import com.example.postservice.entity.PostLike;

public interface IPostLikeService extends IGeneralService<PostLike> {
    boolean existsByPostIdAndAndUserId(String postId, Long userId);
}
