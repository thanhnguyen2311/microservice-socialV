package com.example.noti_service.service.impl;

import com.example.noti_service.dto.GetAllNotiDTO;
import com.example.noti_service.entity.Notifications;
import com.example.noti_service.repository.INotificationRepository;
import com.example.noti_service.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    private INotificationRepository notificationRepository;

    @Override
    public List<Notifications> findAllByUserId(GetAllNotiDTO rq) {
        PageRequest pageRequest = PageRequest.of(rq.getPageIndex(), rq.getPageSize(), Sort.by(Sort.Order.desc("modifiedDate")));
        List<Notifications> notifications = notificationRepository.findAllByRecipientIdOrderByModifiedDateDesc(rq.getUserId(), pageRequest);
        return notifications;
    }
}
