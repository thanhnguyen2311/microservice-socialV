package com.example.messageservice.dto;

import lombok.Data;

@Data
public class CreateMessageReq {
    private Long conversionId;
    private Long userId;
    private String content;
}
