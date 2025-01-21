package com.example.postservice.service;

import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreateCommentDTO;

public interface ICommentService {
    BaseResponse<Object> save(CreateCommentDTO dto, BaseResponse<Object> rp);
}
