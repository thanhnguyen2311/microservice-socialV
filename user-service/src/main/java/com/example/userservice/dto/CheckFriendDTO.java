package com.example.userservice.dto;

import lombok.Data;

@Data
public class CheckFriendDTO {
    private String userId;
    private String friendId;
}
