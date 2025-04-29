package com.example.noti_service.entity;

import com.example.noti_service.enumm.NotificationType;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "RECIPIENT_ID")
    private Long recipientId;
    @Column(name = "ACTOR_ID")
    private Long actorId;
    @Column(name = "POST_ID")
    private String postId;
    @Column(name = "COMMENT_ID")
    private Long commentId;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private Date createdDate;
    private Integer isRead;

}
