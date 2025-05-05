package com.example.noti_service.dto;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private String code = "00";
    private String message = "Success";
    private T data;
}
