package com.example.postservice.service;

import com.example.postservice.dto.*;
import com.example.postservice.entity.Post;

public interface IPostService {
    public BaseResponse<Object> save(CreatePostDTO dto, BaseResponse rp);
    public BaseResponse<Object> update(UpdatePostDTO dto, BaseResponse rp);
    public Post findById(String id);
    public BaseResponse<Object> delete(String id, BaseResponse rp);
    public BaseResponse<Object> findAllByUserId(GetUserPostDTO dto, BaseResponse rp);
    public BaseResponse<Object> findAllPostsFriendWall(GetUserPostDTO dto, BaseResponse rp);
    public BaseResponse<Object> findAllPostsNewFeed(GetUserPostDTO dto, BaseResponse rp);
    public BaseResponse<Object> getPostDetail(PostDetailRq rq, BaseResponse rp);
}
