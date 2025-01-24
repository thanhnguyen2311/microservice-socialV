package com.example.postservice.service;

import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreatePostDTO;
import com.example.postservice.dto.GetUserPostDTO;
import com.example.postservice.dto.UpdatePostDTO;
import com.example.postservice.entity.Post;

import java.util.List;

public interface IPostService {
    public BaseResponse<Object> save(CreatePostDTO dto, BaseResponse rp);
    public BaseResponse<Object> update(UpdatePostDTO dto, BaseResponse rp);
    public Post findById(String id);
    public void delete(Post post);
    public BaseResponse<Object> findAllByUserId(GetUserPostDTO dto, BaseResponse rp);
    public BaseResponse<Object> findAllPostsFriendWall(GetUserPostDTO dto, BaseResponse rp);
    public BaseResponse<Object> findAllPostsNewFeed(GetUserPostDTO dto, BaseResponse rp);
}
