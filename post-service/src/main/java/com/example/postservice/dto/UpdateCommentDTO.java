package com.example.postservice.dto;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;

@Data
public class UpdateCommentDTO {
    private Long id;
    private String content;

    public boolean isValid() {
        return this.id != null && Strings.isNotBlank(content);
    }
}
