package com.example.postservice.service;

import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreatePostDTO;
import com.example.postservice.entity.Post;

public interface IPostService {
    public BaseResponse<Object> save(CreatePostDTO dto, BaseResponse rp);
    public Post findById(long id);
    public void delete(Post post);
}
