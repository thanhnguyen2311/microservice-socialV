package com.example.messageservice.repository;

import com.example.messageservice.entity.ConversationMembers;
import com.example.messageservice.entity.SocialVUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IConversationMembersRepository extends JpaRepository<ConversationMembers, Long> {
    List<ConversationMembers> findAllByConversationId(Long id);
    @Query("select cm.user from ConversationMembers cm where cm.conversation.id = :id")
    List<SocialVUser> findMemberListInChat(Long id);
}
