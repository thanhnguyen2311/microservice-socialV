package com.example.postservice.dto;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;

@Data
public class CreateCommentDTO {
    private String userId;
    private String postId;
    private String content;
    private String commentType;
    private String parentCommentId;

    public boolean isValid() {
        switch (commentType) {
            case "PARENT_CMT":
                return true;
            case "CHILD_CMT":
                return Strings.isNotBlank(parentCommentId);
            default:
                return false;
        }
    }
}
