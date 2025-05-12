package com.example.noti_service.service;

import com.example.noti_service.dto.GetAllNotiDTO;
import com.example.noti_service.entity.Notifications;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface INotificationService {
    List<Notifications> findAllByUserId(GetAllNotiDTO request);
}
