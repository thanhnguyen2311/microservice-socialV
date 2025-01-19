package com.example.postservice.service.impl;

import com.example.postservice.Param;
import com.example.postservice.component.JsonFactory;
import com.example.postservice.component.RestFactory;
import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreatePostDTO;
import com.example.postservice.entity.Post;
import com.example.postservice.enumm.PostStatus;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PostService implements IPostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public BaseResponse<Object> save(CreatePostDTO dto, BaseResponse rp) {
        if (!dto.isValid()) {
            rp.setCode("03");
            rp.setMessage("Thong tin dau vao khong hop le");
            return rp;
        }
        //check exist user
        ResponseEntity<String> res = RestFactory.postUserService(Param.baseUserUrl, Param.FUNCTION_CHECK_USER_EXIST, dto);
        rp = JsonFactory.fromJson(res.getBody(), BaseResponse.class);
        if (rp.isSuccess()) {
            Post post = new Post();
            post.setUserId(Long.parseLong(dto.getUserId()));
            post.setContent(dto.getContent());
            post.setImages(dto.getImages());
            post.setCreatedDate(new Date());
            post.setStatus(PostStatus.getPostStatus(dto.getStatus()));
            postRepository.save(post);
        }
        return rp;
    }

    @Override
    public Post findById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }
}
