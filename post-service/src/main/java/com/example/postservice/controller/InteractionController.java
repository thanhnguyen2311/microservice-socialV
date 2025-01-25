package com.example.postservice.controller;

import com.example.postservice.component.Common;
import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreateCommentDTO;
import com.example.postservice.dto.UpdateCommentDTO;
import com.example.postservice.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interaction")
public class InteractionController {
    @Autowired
    private Common com;
    @Autowired
    private ICommentService commentService;

    @PostMapping("/comment")
    public BaseResponse<Object> comment(@RequestBody CreateCommentDTO dto) {
        BaseResponse<Object> rp = new BaseResponse<>();
        try {
            rp = commentService.save(dto, rp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }
}
