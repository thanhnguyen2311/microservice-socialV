package com.example.noti_service.dto;

import lombok.Data;

@Data
public class NotiCommentDTO {
    private String userId;
    private String postId;
    private String recipientId;
    private String content;
    private String commentType;
    private String parentCommentId;
}
