package com.example.userservice.service;

import com.example.userservice.dto.CheckFriendDTO;
import com.example.userservice.dto.FriendRequestDTO;
import com.example.userservice.dto.MutualFriendDTO;
import com.example.userservice.dto.UserProjectionDTO;
import com.example.userservice.entity.FriendRequest;

import java.util.List;

public interface IFriendRequestService extends IGeneralService<FriendRequest> {
    boolean checkCreateFriendRequest(FriendRequestDTO dto);
    boolean acceptOrRejectFriendRequest(FriendRequestDTO dto);
    List<UserProjectionDTO> getListFriendRequestForUser(Long id);
    List<UserProjectionDTO> getListFriendForUser(Long id);
    List<MutualFriendDTO> getListMutualFriend(Long userId, Long friendId);
    boolean checkIsFriendship(CheckFriendDTO dto);
}
