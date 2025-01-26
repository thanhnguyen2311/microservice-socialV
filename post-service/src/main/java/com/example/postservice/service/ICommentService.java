package com.example.postservice.service;

import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreateCommentDTO;
import com.example.postservice.dto.UpdateCommentDTO;

import java.util.List;

public interface ICommentService {
    BaseResponse<Object> save(CreateCommentDTO dto, BaseResponse<Object> rp);
    public BaseResponse<Object> delete(Long id, BaseResponse rp);
    public BaseResponse<Object> update(UpdateCommentDTO dto, BaseResponse rp);
    List<Long> findAllCommentId();
}
