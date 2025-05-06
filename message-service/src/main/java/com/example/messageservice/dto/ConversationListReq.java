package com.example.messageservice.dto;

import lombok.Data;

@Data
public class ConversationListReq {
    private Long userId;
    private Integer type;
}
