package com.example.noti_service.controller;

import com.example.noti_service.component.Common;
import com.example.noti_service.dto.BaseResponse;
import com.example.noti_service.dto.GetAllNotiDTO;
import com.example.noti_service.entity.Notifications;
import com.example.noti_service.service.INotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private INotificationService notificationService;
    @Autowired
    private Common com;

    @PostMapping("/find-all")
    public BaseResponse<Object> findAll(@RequestBody GetAllNotiDTO request) {
        BaseResponse<Object> response = new BaseResponse<>();
        try {
            List<Notifications> notifications = notificationService.findAllByUserId(request);
            response.setData(notifications);
        } catch (Exception e) {
            log.info(e.getMessage());
            return com.getErrorResponse(response);
        }
        return response;
    }
}
