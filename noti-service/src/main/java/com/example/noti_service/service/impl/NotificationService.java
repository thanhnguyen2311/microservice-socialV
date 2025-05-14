package com.example.noti_service.service.impl;

import com.example.noti_service.component.JsonFactory;
import com.example.noti_service.component.RestFactory;
import com.example.noti_service.dto.BaseResponse;
import com.example.noti_service.dto.GetAllNotiDTO;
import com.example.noti_service.dto.InfoListUserRq;
import com.example.noti_service.dto.UserInfo;
import com.example.noti_service.entity.Notifications;
import com.example.noti_service.param.Param;
import com.example.noti_service.repository.INotificationRepository;
import com.example.noti_service.service.INotificationService;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    private INotificationRepository notificationRepository;

    @Override
    public List<Notifications> findAllByUserId(GetAllNotiDTO rq) {
        PageRequest pageRequest = PageRequest.of(rq.getPageIndex(), rq.getPageSize(), Sort.by(Sort.Order.desc("modifiedDate")));
        List<Notifications> notifications = notificationRepository.findAllByRecipientIdOrderByModifiedDateDesc(rq.getUserId(), pageRequest);
        Set<Long> userIdSet = notifications.stream().map(Notifications::getLatestActorId).collect(Collectors.toSet());
        ResponseEntity<String> res = RestFactory.postUserService(Param.baseUserUrl, Param.FUNCTION_GET_LIST_USER_INFO, new InfoListUserRq(userIdSet));
        Type type = new TypeToken<BaseResponse<List<UserInfo>>>() {}.getType();
        BaseResponse<List<UserInfo>> coreRp = JsonFactory.fromJson(res.getBody(), type);
        Map<String, UserInfo> userInfoMap = coreRp.getData().stream().collect(Collectors.toMap(UserInfo::getId, dto -> dto));
        notifications.forEach(n -> {
            n.setLatestActorInfo(userInfoMap.get(String.valueOf(n.getLatestActorId())));
        });
        return notifications;
    }
}
