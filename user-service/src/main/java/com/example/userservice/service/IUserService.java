package com.example.userservice.service;

import com.example.userservice.dto.UserInfoDTO;
import com.example.userservice.dto.UserProjectionDTO;
import com.example.userservice.entity.SocialVUser;

public interface IUserService extends IGeneralService<SocialVUser>{
    boolean checkExistUser(String id);
    UserInfoDTO findUserInfoById(Long id);
}
