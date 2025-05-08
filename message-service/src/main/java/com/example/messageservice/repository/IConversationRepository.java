package com.example.messageservice.repository;

import com.example.messageservice.entity.Conversation;
import com.example.messageservice.entity.SocialVUser;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IConversationRepository extends JpaRepository<Conversation, Long> {
    @Query(value = "SELECT EXISTS (\n" +
            "               SELECT 1\n" +
            "               FROM conversation_members cm\n" +
            "                        JOIN conversation c ON cm.conversation_id = c.id\n" +
            "               WHERE cm.user_id IN (:userId1, :userId2)\n" +
            "                 AND c.type = 1\n" +
            "               GROUP BY cm.conversation_id\n" +
            "               HAVING COUNT(*) = 2\n" +
            "                  AND COUNT(DISTINCT cm.user_id) = 2\n" +
            "               LIMIT 1\n" +
            "           ) AS conversation_exists", nativeQuery = true)
    Integer checkExistPersonalConversation(Long userId1, Long userId2);

    @Modifying
    @Query(value = "update conversation set last_message_date = :date where id = :id", nativeQuery = true)
    void updateLastMessageDate(@Param("id") Long id, @Param("date") Date date);

    @Query("select cm.conversation from ConversationMembers cm where cm.userId = :userId and (:type is null or cm.conversation.type = :type) order by cm.conversation.lastMessageDate")
    List<Conversation> findConversationsByUserIdAndType(@Param("userId") Long userId,@Param("type") Integer type);
}
