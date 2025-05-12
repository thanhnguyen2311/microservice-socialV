package com.example.postservice.dto;

import lombok.Data;

@Data
public class LikeOrUnLikeDTO {
    private String postId;
    private String userId;
    private String commentId;
    private String type;
}
