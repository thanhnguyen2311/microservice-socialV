package com.example.postservice.dto;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.util.List;

@Data
public class CreatePostDTO {
    private String userId;
    private String content;
    private List<String> images;
    private String status;

    public boolean isValid() {
        return Strings.isNotBlank(userId) && Strings.isNotBlank(content) && Strings.isNotBlank(status);
    }
}
