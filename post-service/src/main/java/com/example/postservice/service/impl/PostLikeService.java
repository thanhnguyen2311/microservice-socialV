package com.example.postservice.service.impl;

import com.example.postservice.Param;
import com.example.postservice.component.JsonFactory;
import com.example.postservice.component.RestFactory;
import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.InfoListUserRq;
import com.example.postservice.dto.UserInfo;
import com.example.postservice.entity.PostLike;
import com.example.postservice.repository.IPostLikeRepository;
import com.example.postservice.service.IPostLikeService;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostLikeService implements IPostLikeService {
    @Autowired
    private IPostLikeRepository postLikeRepository;
    @Override
    public Iterable<PostLike> findAll() {
        return null;
    }

    @Override
    public PostLike findById(Long id) {
        return null;
    }

    @Override
    public PostLike save(PostLike postLike) {
        return postLikeRepository.save(postLike);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public boolean existsByPostIdAndAndUserId(String postId, Long userId) {
        return postLikeRepository.existsByPostIdAndUserId(postId, userId);
    }

    @Override
    public List<UserInfo> getListUserLikePost(String postId) {
        List<PostLike> userIds = postLikeRepository.findAllByPostId(postId);
        Set<Long> userIdSet = userIds.stream().map(PostLike::getUserId).collect(Collectors.toSet());
        ResponseEntity<String> res = RestFactory.postUserService(Param.baseUserUrl, Param.FUNCTION_GET_LIST_USER_INFO, new InfoListUserRq(userIdSet));
        Type type = new TypeToken<BaseResponse<List<UserInfo>>>() {}.getType();
        BaseResponse<List<UserInfo>> coreRp = JsonFactory.fromJson(res.getBody(), type);
        return coreRp.getData();
    }
}
