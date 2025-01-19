package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;


@Entity
@Table(name = "User")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SocialVUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String nickName;
    private String email;
    private String phone;
    private String avatar;
    private Date birthday;
    private String gender;
    private String location;
    private Date createdDate;
    private Date updatedDate;
    private Integer seeFriendPermission;

}
