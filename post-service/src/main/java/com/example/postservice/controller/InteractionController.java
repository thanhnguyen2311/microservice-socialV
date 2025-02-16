package com.example.postservice.controller;

import com.example.postservice.component.Common;
import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreateCommentDTO;
import com.example.postservice.dto.UpdateCommentDTO;
import com.example.postservice.dto.UserInfo;
import com.example.postservice.service.ICommentService;
import com.example.postservice.service.impl.CommentLikeService;
import com.example.postservice.service.impl.PostLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/interaction")
public class InteractionController {
    @Autowired
    private Common com;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private PostLikeService postLikeService;
    @Autowired
    private CommentLikeService commentLikeService;

    @PostMapping("/comment")
    public BaseResponse<Object> comment(@RequestBody CreateCommentDTO dto) {
        BaseResponse<Object> rp = new BaseResponse<>();
        try {
            rp = commentService.save(dto, rp);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(rp);
        }
        return rp;
    }

    @DeleteMapping("/delete-comment/{id}")
    public BaseResponse<Object> deleteComment(@PathVariable Long id) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = commentService.delete(id, response);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @PutMapping("/update-comment")
    public BaseResponse<Object> updateComment(@RequestBody UpdateCommentDTO dto) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = commentService.update(dto, response);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @GetMapping("/list-user-like-post/{id}")
    public BaseResponse<Object> getUserLikePost(@PathVariable String id) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            List<UserInfo> listUserLikePost = postLikeService.getListUserLikePost(id);
            response.setData(listUserLikePost);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @GetMapping("/list-user-like-comment/{id}")
    public BaseResponse<Object> getUserLikeComment(@PathVariable Long id) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            List<UserInfo> listUserLikePost = commentLikeService.findByCommentId(id);
            response.setData(listUserLikePost);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }
}
