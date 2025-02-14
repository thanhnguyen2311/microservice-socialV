package com.example.userservice.service.impl;

import com.example.userservice.component.JsonFactory;
import com.example.userservice.dto.UserInfoDTO;
import com.example.userservice.dto.UserProjectionDTO;
import com.example.userservice.entity.SocialVUser;
import com.example.userservice.repository.IUserRepository;
import com.example.userservice.repository.RedisComponent;
import com.example.userservice.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private RedisComponent cache;

    @Override
    public Iterable<SocialVUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public SocialVUser findById(Long id) {
        String cacheUser = cache.get("userInfo-" + id);
        if (cacheUser != null) {
            log.info("lay tu cache");
            return JsonFactory.fromJson(cacheUser, SocialVUser.class);
        }
        SocialVUser user = userRepository.findById(id).orElse(null);
        cache.save("userInfo-" + id, JsonFactory.toJson(user));
        return user;
    }

    @Override
    public SocialVUser save(SocialVUser socialVUser) {
        return userRepository.save(socialVUser);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean checkExistUser(String id) {
        return userRepository.existsSocialVUserById(Long.parseLong(id));
    }

    @Override
    public UserInfoDTO findUserInfoById(Long id) {
        String cacheUser = cache.get("userInfo-" + id);
        if (cacheUser != null) {
            return JsonFactory.fromJson(cacheUser, UserInfoDTO.class);
        }
        UserInfoDTO user = userRepository.findUserInfoById(id);
        if (user != null) {
            cache.save("userInfo-" + id, JsonFactory.toJson(user));
        }
        return user;
    }

    @Override
    public List<UserInfoDTO> findUserInfoByIds(Set<Long> ids) {
        return userRepository.findUserInfoByIds(ids);
    }
}
