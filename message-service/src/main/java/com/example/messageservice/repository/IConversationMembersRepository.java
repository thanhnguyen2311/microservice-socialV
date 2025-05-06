package com.example.messageservice.repository;

import com.example.messageservice.entity.ConversationMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IConversationMembersRepository extends JpaRepository<ConversationMembers, Long> {
    List<ConversationMembers> findAllByConversationId(Long id);
}
