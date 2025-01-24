package com.example.postservice.dto;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.util.List;

@Data
public class UpdatePostDTO {
    private String id;
    private String content;
    private List<String> images;
    private String status;

    public boolean isValid() {
        return Strings.isNotBlank(content) && Strings.isNotBlank(status) && Strings.isNotBlank(id);
    }
}
