package com.example.userservice.repository;

import com.example.userservice.dto.UserInfoDTO;
import com.example.userservice.dto.UserProjectionDTO;
import com.example.userservice.entity.SocialVUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<SocialVUser, Long> {
    boolean existsSocialVUserById(Long id);

    @Query(value = "select id, nick_name as nickName, avatar from user where id = ?1", nativeQuery = true)
    UserInfoDTO findUserInfoById(Long id);
}
