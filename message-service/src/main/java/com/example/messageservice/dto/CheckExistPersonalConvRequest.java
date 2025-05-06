package com.example.messageservice.dto;

import lombok.Data;

@Data
public class CheckExistPersonalConvRequest {
    private Long firstUser;
    private Long secondUser;
}
