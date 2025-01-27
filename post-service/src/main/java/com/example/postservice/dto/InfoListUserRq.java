package com.example.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class InfoListUserRq {
    private Set<Long> userIds;
}
