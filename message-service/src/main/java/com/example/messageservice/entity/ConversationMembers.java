package com.example.messageservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class ConversationMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "CONVERSATION_ID")
    private Conversation conversation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    private SocialVUser user;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "IS_ACTIVE")
    private Integer isActive;
    @Column(name = "IS_SEEN")
    private Integer isSeen;
}
