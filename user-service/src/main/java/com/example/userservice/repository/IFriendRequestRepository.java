package com.example.userservice.repository;

import com.example.userservice.dto.MutualFriendDTO;
import com.example.userservice.dto.UserProjectionDTO;
import com.example.userservice.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    @Query("SELECT COUNT(f) > 0 FROM FriendRequest f WHERE " +
            "(f.userReceiveId = :userId AND f.userRequestId = :friendId) OR " +
            "(f.userReceiveId = :friendId AND f.userRequestId = :userId)")
    boolean existsFriendshipBetweenUsers(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Query("SELECT COUNT(f) > 0 FROM FriendRequest f WHERE " +
            "(f.userReceiveId = :userId AND f.userRequestId = :friendId AND f.status = 'ACCEPTED') OR " +
            "(f.userReceiveId = :friendId AND f.userRequestId = :userId AND f.status = 'ACCEPTED')")
    boolean isFriend(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Modifying
    @Query(value = "update friend_request set status = 'ACCEPTED' where user_request_id = ?1 and user_receive_id = ?2", nativeQuery = true)
    void acceptFriendRequest(Long userRequestId, Long userReceiveId);

    void deleteFriendRequestByUserReceiveIdAndUserRequestId(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Query(value = "select u.id, u.nick_name as nickName, u.avatar as avatar, fr.created_date as createdDate from friend_request fr join user u on fr.user_request_id = u.id where fr.user_receive_id = ?1 AND fr.status = 'PENDING' order by createdDate DESC", nativeQuery = true)
    List<UserProjectionDTO> findListFriendRequestByUserId(Long userId);

    @Query(value = "select u.id, u.nick_name as nickName, u.avatar as avatar\n" +
            "from user u\n" +
            "where u.id in (SELECT CASE\n" +
            "                          WHEN f.user_receive_id = :userId THEN f.user_request_id\n" +
            "                          ELSE f.user_receive_id\n" +
            "                          END as friendId\n" +
            "               FROM friend_request f\n" +
            "               WHERE :userId IN (f.user_receive_id, f.user_request_id)\n" +
            "                 AND status = 'ACCEPTED')", nativeQuery = true)
    List<UserProjectionDTO> findListFriendByUserId(Long userId);

    @Query(value = "WITH UserFriends AS (SELECT CASE\n" +
            "                                WHEN f.user_receive_id = :userId THEN f.user_request_id\n" +
            "                                ELSE f.user_receive_id\n" +
            "                                END as friendId\n" +
            "                     FROM friend_request f\n" +
            "                     WHERE :userId IN (f.user_receive_id, f.user_request_id)\n" +
            "                       AND status = 'ACCEPTED'),\n" +
            "     TargetFriends AS (SELECT CASE\n" +
            "                                  WHEN f.user_receive_id = :friendId THEN f.user_request_id\n" +
            "                                  ELSE f.user_receive_id\n" +
            "                                  END as friendId\n" +
            "                       FROM friend_request f\n" +
            "                       WHERE :friendId IN (f.user_receive_id, f.user_request_id)\n" +
            "                         AND status = 'ACCEPTED')\n" +
            "SELECT u.id        AS id,\n" +
            "       u.nick_name AS nickName,\n" +
            "       u.avatar    AS avatar\n" +
            "#        COUNT(*)    AS mutualFriendCount\n" +
            "FROM user u\n" +
            "         JOIN UserFriends uf ON u.id = uf.friendId\n" +
            "         JOIN TargetFriends tf ON uf.friendId = tf.friendId", nativeQuery = true)
    List<MutualFriendDTO> findMutualFriendList(Long userId, Long friendId);
}
