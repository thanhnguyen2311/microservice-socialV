package com.example.userservice.controller;

import com.example.userservice.component.Common;
import com.example.userservice.dto.*;
import com.example.userservice.entity.FriendRequest;
import com.example.userservice.service.IFriendRequestService;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private IFriendRequestService friendRequestService;
    @Autowired
    private Common com;
    @Autowired
    private Faker faker;

    @PostMapping("/gen-fake-data")
    public String genFakeData() {
        FriendRequestDTO dto = new FriendRequestDTO();
        for (int i = 0; i < 200; i++) {
            dto.setUserReceiveId((long) faker.number().numberBetween(1, 100));
            dto.setUserRequestId((long) faker.number().numberBetween(1, 100));
            if (friendRequestService.checkCreateFriendRequest(dto)) {
                friendRequestService.save(new FriendRequest(dto));
            }
        }
        return "done";
    }

    @PostMapping
    public BaseResponse<Object> createFriendRequests(@RequestBody FriendRequestDTO dto) {
        BaseResponse<Object> rp = new BaseResponse<>();
        try {
            if (friendRequestService.checkCreateFriendRequest(dto)) {
                friendRequestService.save(new FriendRequest(dto));
            } else {
                rp.setCode("08");
                rp.setMessage("Khong du dieu kien de tao yeu cau ket ban");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(rp);
        }
        return rp;
    }

    @PostMapping("/accept-or-decline-friend")
    public BaseResponse<Object> acceptOrDeclineFriend(@RequestBody FriendRequestDTO dto) {
        BaseResponse<Object> rp = new BaseResponse<>();
        try {
            if (!friendRequestService.acceptOrRejectFriendRequest(dto)) {
                rp.setCode("08");
                rp.setMessage("Khong du dieu kien de tao yeu cau ket ban");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(rp);
        }
        return rp;
    }

    @GetMapping("/list-request/{id}")
    public BaseResponse<Object> getListFriendRequestForUser(@PathVariable Long id) {
        BaseResponse<Object> rp = new BaseResponse<>();
        try {
            List<UserProjectionDTO> friendRqList = friendRequestService.getListFriendRequestForUser(id);
            rp.setData(friendRqList);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(rp);
        }
        return rp;
    }

    @PostMapping("/mutual-fr-list")
    public BaseResponse<Object> getMutualFriendList(@RequestBody FriendRequestDTO dto) {
        MutualFriendRes rp = new MutualFriendRes();
        try {
            List<MutualFriendDTO> friendRqList = friendRequestService.getListMutualFriend(dto.getUserReceiveId(), dto.getUserRequestId());
            rp.setData(friendRqList);
            rp.setCount(friendRqList.size());
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(rp);
        }
        return rp;
    }

    @GetMapping("/list-fr/{id}")
    public BaseResponse<Object> getListFriendForUser(@PathVariable Long id) {
        MutualFriendRes rp = new MutualFriendRes();
        try {
            List<UserProjectionDTO> friendList = friendRequestService.getListFriendForUser(id);
            rp.setData(friendList);
            rp.setCount(friendList.size());
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(rp);
        }
        return rp;
    }

    @PostMapping("/check-friend")
    public BaseResponse<Object> checkFriend(@RequestBody CheckFriendDTO dto) {
        BaseResponse<Object> rp = new BaseResponse<>();
        try {
            if (!friendRequestService.checkIsFriendship(dto)) {
                rp.setCode("03");
                rp.setMessage("Not exist friendship");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(rp);
        }
        return rp;
    }
}
