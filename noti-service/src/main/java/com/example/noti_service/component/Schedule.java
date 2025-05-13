package com.example.noti_service.component;

import com.example.noti_service.dto.LikeOrUnLikeDTO;
import com.example.noti_service.entity.Notifications;
import com.example.noti_service.enumm.NotificationType;
import com.example.noti_service.repository.INotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.example.noti_service.kafka.KafkaConsumer.likePostMap;
import static com.example.noti_service.kafka.KafkaConsumer.unlikePostMap;

import java.util.*;

@Component
@Slf4j
@Transactional
public class Schedule {
    @Autowired
    private INotificationRepository notificationRepository;

    @Scheduled(fixedRate = 8000) // 8s
    public void notiLikePostJob() {
        log.info("notification like post job started");
        Map<String, List<LikeOrUnLikeDTO>> likeMapData = new HashMap<>(likePostMap);
        likePostMap.clear();
        likeMapData.forEach((postId, listUser) -> {
            if (listUser == null || listUser.isEmpty()) return;

            String recipientId = listUser.get(0).getRecipientId();
            String latestUserId = listUser.get(listUser.size() - 1).getUserId();
            int likeCount = listUser.size();

            Notifications notification = notificationRepository
                    .findByPostIdAndType(postId, NotificationType.LIKE)
                    .map(noti -> {
                        noti.setCount(noti.getCount() + likeCount);
                        noti.setLatestActorId(Long.valueOf(latestUserId));
                        noti.setModifiedDate(new Date());
                        noti.setIsRead(0);
                        return notificationRepository.save(noti);
                    })
                    .orElseGet(() -> {
                        Notifications newNoti = new Notifications();
                        newNoti.setRecipientId(Long.valueOf(recipientId));
                        newNoti.setPostId(postId);
                        newNoti.setCount(likeCount);
                        newNoti.setLatestActorId(Long.valueOf(latestUserId));
                        newNoti.setModifiedDate(new Date());
                        newNoti.setType(NotificationType.LIKE);
                        newNoti.setIsRead(0);
                        return notificationRepository.save(newNoti);
                    });
        });
        Map<String, List<LikeOrUnLikeDTO>> unlikeMapData = new HashMap<>(unlikePostMap);
        unlikePostMap.clear();
        unlikeMapData.forEach((postId, listUser) -> {
            Notifications notification = notificationRepository.findByPostIdAndType(postId, NotificationType.LIKE).get();
            if (notification.getCount() == listUser.size()) {
                notificationRepository.delete(notification);
            } else {
                notification.setCount(notification.getCount() - listUser.size());
                notificationRepository.save(notification);
            }
        });
        log.info("notification like post job done");
    }
}
