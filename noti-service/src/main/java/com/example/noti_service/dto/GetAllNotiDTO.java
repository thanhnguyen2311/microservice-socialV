package com.example.noti_service.dto;

import lombok.Data;

@Data
public class GetAllNotiDTO {
    private Long userId;
    private Integer pageIndex;
    private Integer pageSize;
}
