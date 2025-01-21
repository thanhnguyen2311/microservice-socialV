package com.example.postservice.controller;

import com.example.postservice.component.Common;
import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreateCommentDTO;
import com.example.postservice.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
