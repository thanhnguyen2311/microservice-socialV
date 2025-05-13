package com.example.noti_service.dto;

import lombok.Data;

@Data
public class LikeOrUnLikeDTO {
    private String postId;
    private String userId; //nguoi thao tac
    private String commentId;
    private String recipientId; // chu post hoac chu comment
    private String type;
}
