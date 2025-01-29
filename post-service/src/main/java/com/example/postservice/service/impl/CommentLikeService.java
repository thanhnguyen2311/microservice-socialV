package com.example.postservice.service.impl;

import com.example.postservice.Param;
import com.example.postservice.component.JsonFactory;
import com.example.postservice.component.RestFactory;
import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.InfoListUserRq;
import com.example.postservice.dto.UserInfo;
import com.example.postservice.entity.CommentLike;
import com.example.postservice.repository.ICommentLikeRepository;
import com.example.postservice.service.ICommentLikeService;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentLikeService implements ICommentLikeService {
    @Autowired
    private ICommentLikeRepository commentLikeRepository;

    @Override
    public boolean existsByCommentIdAndUserId(Long commentId, Long userId) {
        return commentLikeRepository.existsByCommentIdAndUserId(commentId, userId);
    }

    @Override
    public CommentLike save(CommentLike commentLike) {
        return commentLikeRepository.save(commentLike);
    }

    @Override
    public List<UserInfo> findByCommentId(Long commentId) {
        List<CommentLike> userIds = commentLikeRepository.findAllByCommentId(commentId);
        Set<Long> userIdSet = userIds.stream().map(CommentLike::getUserId).collect(Collectors.toSet());
        ResponseEntity<String> res = RestFactory.postUserService(Param.baseUserUrl, Param.FUNCTION_GET_LIST_USER_INFO, new InfoListUserRq(userIdSet));
        Type type = new TypeToken<BaseResponse<List<UserInfo>>>() {}.getType();
        BaseResponse<List<UserInfo>> coreRp = JsonFactory.fromJson(res.getBody(), type);
        return coreRp.getData();
    }
}
