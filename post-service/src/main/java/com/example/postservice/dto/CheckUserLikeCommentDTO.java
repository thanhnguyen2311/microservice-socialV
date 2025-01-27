package com.example.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckUserLikeCommentDTO {
    private Long commentId;
    private Integer hasLiked;
}
