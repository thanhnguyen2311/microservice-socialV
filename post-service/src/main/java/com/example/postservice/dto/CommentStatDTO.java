package com.example.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentStatDTO {
    private Long commentId;
    private Long countLike;
}
