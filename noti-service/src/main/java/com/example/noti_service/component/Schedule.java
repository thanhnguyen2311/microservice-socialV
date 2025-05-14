package com.example.noti_service.component;

import com.example.noti_service.dto.LikeOrUnLikeDTO;
import com.example.noti_service.dto.NotiCommentDTO;
import com.example.noti_service.entity.Notifications;
import com.example.noti_service.enumm.NotificationType;
import com.example.noti_service.repository.INotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.noti_service.kafka.KafkaConsumer.*;

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
            if (listUser == null || listUser.isEmpty()) return;
            Notifications notification = notificationRepository.findByPostIdAndType(postId, NotificationType.LIKE).get();
            if (notification.getCount() == listUser.size()) {
                notificationRepository.delete(notification);
            } else {
                String recipientId = listUser.get(0).getRecipientId();
                notification.setCount(notification.getCount() - listUser.size());
                notification.setLatestActorId(notificationRepository.latestUserLikePost(postId, recipientId));
                notificationRepository.save(notification);
            }
        });
        log.info("notification like post job done");
    }

    @Scheduled(fixedRate = 8000) // 8s
    public void notiCommentPostJob() {
        log.info("notification comment post job started");
        Map<String, List<NotiCommentDTO>> commentMapData = new HashMap<>(commentMap);
        commentMap.clear();
        commentMapData.forEach((postId, dto) -> {
            String recipientId = dto.get(0).getRecipientId();
            String latestUserId = dto.get(dto.size() - 1).getUserId();
            Notifications notifications = notificationRepository
                    .findByPostIdAndType(postId, NotificationType.COMMENT)
                    .map(noti -> {
                        noti.setCount(notificationRepository.countUserCommentPost(postId,recipientId));
                        noti.setLatestActorId(Long.valueOf(latestUserId));
                        noti.setModifiedDate(new Date());
                        noti.setIsRead(0);
                        return notificationRepository.save(noti);
                    })
                    .orElseGet(() -> {
                        Notifications newNoti = new Notifications();
                        newNoti.setRecipientId(Long.valueOf(recipientId));
                        newNoti.setPostId(postId);
                        newNoti.setCount(notificationRepository.countUserCommentPost(postId,recipientId));
                        newNoti.setLatestActorId(Long.valueOf(latestUserId));
                        newNoti.setModifiedDate(new Date());
                        newNoti.setType(NotificationType.COMMENT);
                        newNoti.setIsRead(0);
                        return notificationRepository.save(newNoti);
                    });
        });
        log.info("notification comment post job done");
    }
}
