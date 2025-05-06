package com.example.messageservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateConversationReq {
    private Integer type;
    private List<Long> userIds;
    private String createdBy;
}
