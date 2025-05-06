package com.example.messageservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // type = 1 là cuộc hội thoại cá nhân, type = 2 là cuộc hội thoại nhóm
    private Integer type;
    private String name;
    private Date createdDate;
    private String avatarConversationUrl;
    private String createdBy;
    private Date modifiedDate;
    private Date lastMessageDate;
}
