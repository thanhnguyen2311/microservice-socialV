package com.example.postservice.service.impl;

import com.example.postservice.entity.CommentLike;
import com.example.postservice.repository.ICommentLikeRepository;
import com.example.postservice.service.ICommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentLikeService implements ICommentLikeService {
    @Autowired
    private ICommentLikeRepository commentLikeRepository;

    @Override
    public boolean existsByCommentIdAndUserId(Long commentId, Long userId) {
        return commentLikeRepository.existsByCommentIdAndUserId(commentId, userId);
    }

    @Override
    public CommentLike save(CommentLike commentLike) {
        return commentLikeRepository.save(commentLike);
    }
}
