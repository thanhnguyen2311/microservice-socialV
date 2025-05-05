package com.example.postservice.dto;

import lombok.Data;

@Data
public class LikeOrUnLikePostDTO {
    private String postId;
    private String userId;
    private String type;
}
