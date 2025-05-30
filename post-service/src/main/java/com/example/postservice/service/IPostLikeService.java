package com.example.postservice.service;

import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.LikeOrUnLikeDTO;
import com.example.postservice.dto.UserInfo;
import com.example.postservice.entity.PostLike;

import java.util.List;

public interface IPostLikeService extends IGeneralService<PostLike> {
    boolean existsByPostIdAndAndUserId(String postId, Long userId);
    List<UserInfo> getListUserLikePost(String postId);
    BaseResponse<Object> likeOrUnlikePost(LikeOrUnLikeDTO dto, BaseResponse rp);
}
