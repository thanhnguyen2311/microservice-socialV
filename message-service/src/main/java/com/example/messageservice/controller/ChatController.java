package com.example.messageservice.controller;

import com.example.messageservice.component.Common;
import com.example.messageservice.dto.*;
import com.example.messageservice.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private Common com;
    @Autowired
    private IMessageService messageService;
    @GetMapping("/get-conversation/{conversationId}")
    public BaseResponse<Object> getConversation(@PathVariable Long conversationId) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = messageService.findAllByConversation(conversationId, response);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @PostMapping ("/conversation-list")
    public BaseResponse<Object> getConversationList(@RequestBody ConversationListReq rq) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = messageService.findAllConversationByUser(rq, response);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @PostMapping("/create-conversation")
    public BaseResponse<Object> createConversation(@RequestBody CreateConversationReq rq) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = messageService.createConversation(rq, response);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    //fix chỉ lấy tên và avatar
    @GetMapping("/member-list/{conversationId}")
    public BaseResponse<Object> getMemberListConversation(@PathVariable Long conversationId) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = messageService.findMemberListInChat(conversationId, response);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @PostMapping("/create-message")
    public BaseResponse<Object> createMessage(@RequestBody CreateMessageReq rq) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = messageService.createMessage(rq, response);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @PutMapping("/change-group-name")
    public BaseResponse<Object> changeGroupName(@RequestBody ChangeGroupNameReq rq) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            messageService.updateGroupChatName(rq.getConversionId(),rq.getName());
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @PostMapping("/check-exist-personal-conv")
    public BaseResponse<Object> checkExistPersonalConversation(@RequestBody CheckExistPersonalConvRequest rq) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response = messageService.checkExistPersonalConversation(rq, response);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }
}
