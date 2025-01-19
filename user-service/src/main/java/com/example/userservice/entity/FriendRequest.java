package com.example.userservice.entity;

import com.example.userservice.dto.FriendRequestDTO;
import com.example.userservice.enumm.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "FRIEND_REQUEST")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userRequest_id",referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    private SocialVUser userRequest;
    @Column(name = "userRequest_id")
    private Long userRequestId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userReceive_id",referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    private SocialVUser userReceive;
    @Column(name = "userReceive_id")
    private Long userReceiveId;
    private Date createdDate;
    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

    public FriendRequest(FriendRequestDTO dto) {
        this.userRequestId = dto.getUserRequestId();
        this.userReceiveId = dto.getUserReceiveId();
        this.createdDate = new Date();
        this.status = FriendRequestStatus.PENDING;
    }
}
