package com.example.messageservice.repository;

import com.example.messageservice.dto.UserInfoDTO;
import com.example.messageservice.entity.ConversationMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IConversationMembersRepository extends JpaRepository<ConversationMembers, Long> {
    List<ConversationMembers> findAllByConversationId(Long id);
    @Query(value = "select u.id, u.nick_name as nickName, avatar from user u join conversation_members cm on u.id = cm.user_id where cm.conversation_id = :id", nativeQuery = true)
    List<UserInfoDTO> findMemberListInChat(@Param("id")Long id);
}
