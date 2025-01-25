package com.example.postservice.controller;

import com.example.postservice.component.Common;
import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreatePostDTO;
import com.example.postservice.dto.GetUserPostDTO;
import com.example.postservice.dto.UpdatePostDTO;
import com.example.postservice.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            response = postService.findAllByUserId(dto, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @PostMapping("/get-post-friend-wall")
    public BaseResponse<Object> getPostFriendWall(@RequestBody GetUserPostDTO dto) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = postService.findAllPostsFriendWall(dto, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @PostMapping("/new-feed")
    public BaseResponse<Object> getNewFeed(@RequestBody GetUserPostDTO dto) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = postService.findAllPostsNewFeed(dto, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @PutMapping("/update")
    public BaseResponse<Object> updatePost(@RequestBody UpdatePostDTO postDto) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = postService.update(postDto, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Object> deletePost(@PathVariable String id) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = postService.delete(id, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }
}
