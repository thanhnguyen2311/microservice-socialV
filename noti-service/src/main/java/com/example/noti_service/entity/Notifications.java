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
    @Column(name = "POST_ID")
    private String postId;
    @Column(name = "COMMENT_ID")
    private Long commentId;
    private Integer count; //số lượng tương tác
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LATEST_USER_ID", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    private SocialVUser latestActor;
    @Column(name = "LATEST_USER_ID")
    private Long latestActorId;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private Date modifiedDate;
    private Integer isRead;

}
