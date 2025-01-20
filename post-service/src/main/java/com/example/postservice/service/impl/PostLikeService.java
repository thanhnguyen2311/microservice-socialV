package com.example.postservice.service.impl;

import com.example.postservice.entity.PostLike;
import com.example.postservice.repository.IPostLikeRepository;
import com.example.postservice.service.IPostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostLikeService implements IPostLikeService {
    @Autowired
    private IPostLikeRepository postLikeRepository;
    @Override
    public Iterable<PostLike> findAll() {
        return null;
    }

    @Override
    public PostLike findById(Long id) {
        return null;
    }

    @Override
    public PostLike save(PostLike postLike) {
        return postLikeRepository.save(postLike);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public boolean existsByPostIdAndAndUserId(String postId, Long userId) {
        return postLikeRepository.existsByPostIdAndUserId(postId, userId);
    }
}
