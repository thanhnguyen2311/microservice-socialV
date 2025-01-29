package com.example.postservice.repository;

import com.example.postservice.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {
    void deleteAllByParentCommentId(Long parentCommentId);
    Integer countAllByPostId(String postId);
    Page<Comment> findAllByPostId(String postId, PageRequest pageRequest);
}
