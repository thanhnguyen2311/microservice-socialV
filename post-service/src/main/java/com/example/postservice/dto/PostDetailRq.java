package com.example.postservice.dto;

import lombok.Data;

@Data
public class PostDetailRq {
    private String postId;
    private String userId;
    private int pageIndex;
    private int pageSize;
}
