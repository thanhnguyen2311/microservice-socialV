package com.example.messageservice.service;

import com.example.messageservice.dto.BaseResponse;
import com.example.messageservice.dto.CheckExistPersonalConvRequest;
import com.example.messageservice.dto.CreateConversationReq;
import com.example.messageservice.dto.CreateMessageReq;
import com.example.messageservice.entity.Message;

import java.util.List;

public interface IMessageService {
    BaseResponse<Object> findAllByConversation(Long id, BaseResponse<Object> rp);
    BaseResponse<Object> createConversation(CreateConversationReq rq, BaseResponse<Object> rp);
    BaseResponse<Object> createMessage(CreateMessageReq rq, BaseResponse<Object> rp);
    BaseResponse<Object> checkExistPersonalConversation(CheckExistPersonalConvRequest rq, BaseResponse<Object> rp);
}
