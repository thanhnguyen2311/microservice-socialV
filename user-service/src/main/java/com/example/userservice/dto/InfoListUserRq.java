package com.example.userservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class InfoListUserRq {
    private Set<Long> userIds;
}
