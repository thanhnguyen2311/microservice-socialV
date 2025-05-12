package com.example.noti_service.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "NOTIFICATION_ACTOR")
public class NotificationActor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long notificationId;
    private Long actorId;
    private Date createdDate;
}
