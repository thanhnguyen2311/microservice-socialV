package com.example.messageservice.dto;

import lombok.Data;

@Data
public class ChangeGroupNameReq {
    private Long conversionId;
    private String name;
}
