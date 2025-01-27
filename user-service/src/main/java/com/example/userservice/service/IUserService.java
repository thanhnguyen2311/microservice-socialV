package com.example.userservice.service;

import com.example.userservice.dto.UserInfoDTO;
import com.example.userservice.entity.SocialVUser;

import java.util.List;
import java.util.Set;

public interface IUserService extends IGeneralService<SocialVUser>{
    boolean checkExistUser(String id);
    UserInfoDTO findUserInfoById(Long id);
    List<UserInfoDTO> findUserInfoByIds(Set<Long> ids);
}
