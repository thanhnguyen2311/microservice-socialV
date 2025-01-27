package com.example.userservice.repository;

import com.example.userservice.dto.UserInfoDTO;
import com.example.userservice.dto.UserProjectionDTO;
import com.example.userservice.entity.SocialVUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IUserRepository extends JpaRepository<SocialVUser, Long> {
    boolean existsSocialVUserById(Long id);

    @Query(value = "select id, nick_name as nickName, avatar from user where id = ?1", nativeQuery = true)
    UserInfoDTO findUserInfoById(Long id);

    @Query(value = "select id, nick_name as nickName, avatar from user where id in :userIds", nativeQuery = true)
    List<UserInfoDTO> findUserInfoByIds(@Param("userIds") Set<Long> userIds);
}
