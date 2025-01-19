package com.example.userservice.controller;

import com.example.userservice.component.Common;
import com.example.userservice.dto.BaseResponse;
import com.example.userservice.entity.SocialVUser;
import com.example.userservice.service.impl.UserService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private Common com;
    private Faker faker = new Faker();

    @GetMapping
    public BaseResponse<Object> getAllUsers() {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            Iterable<SocialVUser> users = userService.findAll();
            response.setData(users);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }
}
