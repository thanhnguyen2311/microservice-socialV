package com.example.postservice.controller;

import com.example.postservice.component.Common;
import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreatePostDTO;
import com.example.postservice.dto.GetUserPostDTO;
import com.example.postservice.entity.Post;
import com.example.postservice.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private IPostService postService;
    @Autowired
    private Common com;

    @PostMapping("/create")
    public BaseResponse<Object> createPost(@RequestBody CreatePostDTO postDto) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = postService.save(postDto, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;

    }

    @PostMapping("/get-all-by-user")
    public BaseResponse<Object> getAllByUser(@RequestBody GetUserPostDTO dto) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            List<Post> postList = postService.findAllByUserId(dto);
            response.setData(postList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }


}
