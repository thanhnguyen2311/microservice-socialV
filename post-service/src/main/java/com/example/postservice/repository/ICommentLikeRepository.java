package com.example.postservice.repository;

import com.example.postservice.dto.CheckUserLikeCommentDTO;
import com.example.postservice.dto.CommentStatDTO;
import com.example.postservice.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ICommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Boolean existsByCommentIdAndUserId(Long commentId, Long userId);

    @Query("SELECT NEW com.example.postservice.dto.CommentStatDTO(cl.commentId, COUNT(cl)) " +
            "FROM CommentLike cl " +
            "WHERE cl.commentId IN :commentIds GROUP BY cl.commentId")
    List<CommentStatDTO> countLikeListComment(@Param("commentIds") Set<Long> commentIds);

    @Query("SELECT NEW com.example.postservice.dto.CheckUserLikeCommentDTO(cl1.commentId, " +
            "CASE WHEN (SELECT COUNT(cl2) FROM CommentLike cl2 WHERE cl2.commentId = cl1.commentId AND cl2.userId = :userId) > 0 THEN 1 ELSE 0 END) " +
            "FROM CommentLike cl1 " +
            "WHERE cl1.commentId IN :commentIds " +
            "GROUP BY cl1.commentId")
    List<CheckUserLikeCommentDTO> checkLikeListComment(@Param("commentIds") Set<Long> commentIds, @Param("userId") Long userId);

    List<CommentLike> findAllByCommentId(Long commentId);
    void deleteAllByCommentIdAndUserId(Long commentId, Long userId);
}
