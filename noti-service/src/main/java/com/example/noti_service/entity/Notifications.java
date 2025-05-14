package com.example.noti_service.entity;

import com.example.noti_service.dto.UserInfo;
import com.example.noti_service.enumm.NotificationType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table
@Data
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "RECIPIENT_ID")
    private Long recipientId;
    @Column(name = "POST_ID")
    private String postId;
    @Column(name = "COMMENT_ID")
    private Long commentId;
    private Integer count; //số lượng tương tác
    @Column(name = "LATEST_USER_ID")
    private Long latestActorId;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private Date modifiedDate;
    private Integer isRead;
    @Transient
    private UserInfo latestActorInfo;
}
