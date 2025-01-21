package com.example.postservice.dto;

import lombok.Data;

@Data
public class GetUserPostDTO {
    private String userId;
    private String friendId;
    private int pageIndex;
    private int pageSize;
}
