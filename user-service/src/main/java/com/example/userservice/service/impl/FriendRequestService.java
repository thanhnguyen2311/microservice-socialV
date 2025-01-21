package com.example.userservice.service.impl;

import com.example.userservice.dto.CheckFriendDTO;
import com.example.userservice.dto.FriendRequestDTO;
import com.example.userservice.dto.MutualFriendDTO;
import com.example.userservice.dto.UserProjectionDTO;
import com.example.userservice.entity.FriendRequest;
import com.example.userservice.repository.IFriendRequestRepository;
import com.example.userservice.repository.IUserRepository;
import com.example.userservice.service.IFriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FriendRequestService implements IFriendRequestService {
    @Autowired
    private IFriendRequestRepository friendRequestRepository;
    @Autowired
    private IUserRepository userRepository;

    @Override
    public Iterable<FriendRequest> findAll() {
        return friendRequestRepository.findAll();
    }

    @Override
    public FriendRequest findById(Long id) {
        FriendRequest friendRequest = friendRequestRepository.findById(id).orElse(null);
        return friendRequest;
    }

    @Override
    public FriendRequest save(FriendRequest friendRequest) {
        return friendRequestRepository.save(friendRequest);
    }

    @Override
    public void remove(Long id) {
        friendRequestRepository.deleteById(id);
    }

    @Override
    public boolean checkCreateFriendRequest(FriendRequestDTO dto) {
        if (dto.getUserRequestId().equals(dto.getUserReceiveId())) return false;
        if (!userRepository.existsSocialVUserById(dto.getUserReceiveId())) return false;
        if (!userRepository.existsSocialVUserById(dto.getUserRequestId())) return false;
        if (friendRequestRepository.existsFriendshipBetweenUsers(dto.getUserRequestId(), dto.getUserReceiveId()))
            return false;
        return true;
    }

    @Override
    public boolean acceptOrRejectFriendRequest(FriendRequestDTO dto) {
        if (!dto.isValid()) return false;
        if (dto.getAction().equals("ACCEPT")) {
            friendRequestRepository.acceptFriendRequest(dto.getUserRequestId(), dto.getUserReceiveId());
        } else {
            friendRequestRepository.deleteFriendRequestByUserReceiveIdAndUserRequestId(dto.getUserReceiveId(), dto.getUserRequestId());
        }
        return true;
    }

    @Override
    public List<UserProjectionDTO> getListFriendRequestForUser(Long id) {
        return friendRequestRepository.findListFriendRequestByUserId(id);
    }

    @Override
    public List<UserProjectionDTO> getListFriendForUser(Long id) {
        return friendRequestRepository.findListFriendByUserId(id);
    }

    @Override
    public List<MutualFriendDTO> getListMutualFriend(Long userId, Long friendId) {
        return friendRequestRepository.findMutualFriendList(userId, friendId);
    }

    @Override
    public boolean checkIsFriendship(CheckFriendDTO dto) {
        return friendRequestRepository.isFriend(Long.parseLong(dto.getUserId()), Long.parseLong(dto.getFriendId()));
    }
}
