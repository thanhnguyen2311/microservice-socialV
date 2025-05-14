package com.example.noti_service.repository;

import com.example.noti_service.entity.Notifications;
import com.example.noti_service.enumm.NotificationType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface INotificationRepository extends JpaRepository<Notifications, Long> {
    List<Notifications> findAllByRecipientIdOrderByModifiedDateDesc(Long userId, PageRequest pageRequest);
    Optional<Notifications> findByPostIdAndType(String postId, NotificationType type);
    Optional<Notifications> findByCommentIdAndRecipientId(Long commentId, Long recipientId);
    @Query(value = "select user_id from post_like where post_id = ?1 and user_id != ?2 order by created_date desc limit 1", nativeQuery = true)
    Long latestUserLikePost(String postId, String ownerId);
    @Query(value = "select count(distinct user_id) from comment where post_id = ?1 and user_id != ?2", nativeQuery = true)
    Integer countUserCommentPost(String postId, String ownerId);
}
