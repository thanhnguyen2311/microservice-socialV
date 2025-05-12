package com.example.noti_service.repository;

import com.example.noti_service.entity.Notifications;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INotificationRepository extends JpaRepository<Notifications, Long> {
    List<Notifications> findAllByRecipientIdOrderByModifiedDateDesc(Long userId, PageRequest pageRequest);
}
