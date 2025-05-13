package com.example.noti_service.repository;

import com.example.noti_service.entity.Notifications;
import com.example.noti_service.enumm.NotificationType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface INotificationRepository extends JpaRepository<Notifications, Long> {
    List<Notifications> findAllByRecipientIdOrderByModifiedDateDesc(Long userId, PageRequest pageRequest);
    Optional<Notifications> findByPostIdAndType(String postId, NotificationType type);
    Optional<Notifications> findByCommentIdAndRecipientId(Long commentId, Long recipientId);
}
