package com.example.userservice.controller;

import com.example.userservice.component.Common;
import com.example.userservice.dto.BaseResponse;
import com.example.userservice.dto.CheckUserDTO;
import com.example.userservice.dto.InfoListUserRq;
import com.example.userservice.entity.SocialVUser;
import com.example.userservice.service.impl.UserService;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private Common com;
    @Autowired
    private Faker faker;

    @GetMapping
    public BaseResponse<Object> getAllUsers() {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            Iterable<SocialVUser> users = userService.findAll();
            response.setData(users);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @PostMapping
    public void genRandomUser() {
        for (int i = 0; i < 100; i++) {
            SocialVUser user = new SocialVUser();
            user.setUsername(faker.name().username());
            user.setPassword(faker.internet().password());
            user.setNickName(faker.name().firstName());
            user.setEmail(faker.internet().emailAddress());
            user.setPhone(faker.phoneNumber().phoneNumber());
            user.setAvatar(faker.internet().image());
            user.setBirthday(faker.date().birthday());
            user.setGender(faker.options().option("Male", "Female"));
            user.setLocation(faker.address().city());
            user.setCreatedDate(faker.date().past(365, java.util.concurrent.TimeUnit.DAYS));
            user.setUpdatedDate(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS));
            user.setSeeFriendPermission(faker.number().numberBetween(0, 1));

            userService.save(user);
        }
    }

    @GetMapping("/{id}")
    public BaseResponse<Object> getUserById(@PathVariable Long id) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response.setData(userService.findById(id));
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @PostMapping("/check-exist")
    public BaseResponse<Object> checkUserExist(@RequestBody CheckUserDTO dto) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            if (!userService.checkExistUser(dto.getUserId())) {
                response.setCode("05");
                response.setMessage("User not found");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @GetMapping("/info/{id}")
    public BaseResponse<Object> getUserInfoById(@PathVariable Long id) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response.setData(userService.findUserInfoById(id));
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }

    @PostMapping("/info-list-user")
    public BaseResponse<Object> getUserInfoByIds(@RequestBody InfoListUserRq rq) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            response.setData(userService.findUserInfoByIds(rq.getUserIds()));
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }
}
