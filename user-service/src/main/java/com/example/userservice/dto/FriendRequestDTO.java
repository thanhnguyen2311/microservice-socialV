package com.example.userservice.dto;

import lombok.Data;
import java.util.Arrays;

@Data
public class FriendRequestDTO {
    private Long userRequestId;
    private Long userReceiveId;
    private String action;

    public boolean isValid() {
        return userReceiveId != null && userRequestId != null && Arrays.asList("ACCEPT", "DECLINE").contains(action);
    }
}
