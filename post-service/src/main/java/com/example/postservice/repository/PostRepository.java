package com.example.postservice.repository;

import com.example.postservice.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, Long> {
    Page<Post> findAllByUserIdOrderByCreatedDateDesc(Long userId, PageRequest pageRequest);
    Page<Post> findAllByUserIdAndStatusInOrderByCreatedDateDesc(Long userId, List<String> statuses, PageRequest pageRequest);
}
