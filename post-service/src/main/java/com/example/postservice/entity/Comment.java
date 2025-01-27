package com.example.postservice.entity;

import com.example.postservice.dto.UserInfo;
import com.example.postservice.enumm.CommentType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String postId;
    private Long userId;
    private String content;
    private Date createdDate;
    private Date modifiedDate;
    @Enumerated(EnumType.STRING)
    private CommentType type;
    @Column(name = "PARENT_COMMENT_ID")
    private Long parentCommentId;
    @Transient
    private Long countLike;
    @Transient
    private Integer check_user_like;
    @Transient
    private UserInfo userInfo;
}
