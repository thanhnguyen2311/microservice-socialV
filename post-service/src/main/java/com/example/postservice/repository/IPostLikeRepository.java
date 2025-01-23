package com.example.postservice.repository;

import com.example.postservice.dto.CheckUserLikeDTO;
import com.example.postservice.dto.PostStatDTO;
import com.example.postservice.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IPostLikeRepository extends JpaRepository<PostLike, Long> {
    Boolean existsByPostIdAndUserId(String postId, Long userId);

    @Query(value = "SELECT p.post_id as postId,\n" +
            "       COALESCE(l.countLike, 0)    as likeCount,\n" +
            "       COALESCE(c.countComment, 0) as commentCount\n" +
            "FROM (SELECT DISTINCT post_id\n" +
            "      FROM post_like\n" +
            "      WHERE post_id IN (:postIds)) p\n" +
            "         LEFT JOIN\n" +
            "     (SELECT post_id, COUNT(*) as countLike\n" +
            "      FROM post_like\n" +
            "      GROUP BY post_id) l ON p.post_id = l.post_id\n" +
            "         LEFT JOIN\n" +
            "     (SELECT post_id, COUNT(*) as countComment\n" +
            "      FROM comment\n" +
            "      GROUP BY post_id) c ON p.post_id = c.post_id;", nativeQuery = true)
    List<PostStatDTO> findPostStatsByIds(@Param("postIds") Set<String> postIds);

    @Query(value = "SELECT p.post_id                                         AS postId,\n" +
            "       CASE WHEN l.post_id IS NOT NULL THEN 1 ELSE 0 END AS hasLiked\n" +
            "FROM (SELECT DISTINCT post_id\n" +
            "      FROM post_like\n" +
            "      WHERE post_id IN (:postIds)) p\n" +
            "         LEFT JOIN post_like l\n" +
            "                   ON p.post_id = l.post_id AND l.user_id = :userId;", nativeQuery = true)
    List<CheckUserLikeDTO> findCheckUserLikesByPostIdAndUserId(@Param("postIds") Set<String> postIds, @Param("userId") Long userId);


}
