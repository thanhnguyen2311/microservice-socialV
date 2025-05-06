package com.example.messageservice.repository;

import com.example.messageservice.dto.BaseResponse;
import com.example.messageservice.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {
    BaseResponse<Object> findAllByConversationId(Long id);
}
