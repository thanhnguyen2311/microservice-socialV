package com.example.messageservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONVERSATION_ID", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    private Conversation conversation;
    @Column(name = "CONVERSATION_ID")
    private Long conversationId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    private SocialVUser user;
    @Column(name = "USER_ID")
    private Long userId;
    private Date createdDate;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
}
