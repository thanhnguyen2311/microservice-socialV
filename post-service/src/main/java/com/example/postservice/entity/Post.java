package com.example.postservice.entity;

import com.example.postservice.enumm.PostStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "Post")
@Data
public class Post {
    @Id
    private String id;
    private Long userId;
    private String content;
    private List<String> images;
    private Date createdDate;
    private Date modifiedDate;
    @Enumerated(EnumType.STRING)
    private PostStatus status;
    @Transient
    private Integer check_user_like;
    @Transient
    private Integer countLike;
    @Transient
    private Integer countComment;
}
