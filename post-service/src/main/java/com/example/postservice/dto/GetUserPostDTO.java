package com.example.postservice.dto;

import lombok.Data;

@Data
public class GetUserPostDTO {
    private String userId;
    private int pageIndex;
    private int pageSize;
}
