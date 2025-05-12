package com.example.postservice.component;

import com.example.postservice.dto.LikeOrUnLikeDTO;
import com.example.postservice.entity.CommentLike;
import com.example.postservice.entity.PostLike;
import com.example.postservice.repository.ICommentLikeRepository;
import com.example.postservice.repository.IPostLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import static com.example.postservice.kafka.KafkaConsumer.likePostMap;
import static com.example.postservice.kafka.KafkaConsumer.unlikePostMap;
import static com.example.postservice.kafka.KafkaConsumer.likeCommentMap;
import static com.example.postservice.kafka.KafkaConsumer.unlikeCommentMap;

@Component
@Slf4j
@Transactional
public class Schedule {
    @Autowired
    private IPostLikeRepository postLikeRepository;
    @Autowired
    private ICommentLikeRepository commentLikeRepository;

    @Scheduled(fixedRate = 2000) // 2s
    public void likePostJob() {
        Map<String, LikeOrUnLikeDTO> likeMapData = new HashMap<>(likePostMap);
        likePostMap.clear();
        log.info("like post job started");
        List<PostLike> postLikes = new ArrayList<>();
        //goi vao DB de insert hang loat ban ghi like
        likeMapData.forEach((key, value) -> {
            PostLike postLike = new PostLike();
            postLike.setUserId(Long.parseLong(value.getUserId()));
            postLike.setPostId(value.getPostId());
            postLike.setCreatedDate(new Date());
            postLikes.add(postLike);
        });
        postLikeRepository.saveAll(postLikes);
        log.info("like post job finished");
    }

    @Scheduled(fixedRate = 2000) // 2s
    public void unlikePostJob() {
        Map<String, LikeOrUnLikeDTO> unlikeMapData = new HashMap<>(unlikePostMap);
        unlikePostMap.clear();
        log.info("unlike post job started");
        //goi vao DB de insert hang loat ban ghi like
        unlikeMapData.forEach((key, value) -> {
            postLikeRepository.deleteAllByPostIdAndUserId(value.getPostId(), Long.valueOf(value.getUserId()));
        });
        log.info("unlike post job finished");
    }

    @Scheduled(fixedRate = 2000) // 2s
    public void likeCommentJob() {
        Map<String, LikeOrUnLikeDTO> likeMapData = new HashMap<>(likeCommentMap);
        likeCommentMap.clear();
        log.info("like comment job started");
        List<CommentLike> cmtLikes = new ArrayList<>();
        //goi vao DB de insert hang loat ban ghi like
        likeMapData.forEach((key, value) -> {
            CommentLike cmtLike = new CommentLike();
            cmtLike.setUserId(Long.parseLong(value.getUserId()));
            cmtLike.setCommentId(Long.parseLong(value.getCommentId()));
            cmtLike.setCreatedDate(new Date());
            cmtLikes.add(cmtLike);
        });
        commentLikeRepository.saveAll(cmtLikes);
        log.info("like comment job finished");
    }

    @Scheduled(fixedRate = 2000) // 2s
    public void unlikeCommentJob() {
        Map<String, LikeOrUnLikeDTO> unlikeMapData = new HashMap<>(unlikeCommentMap);
        unlikeCommentMap.clear();
        log.info("unlike comment job started");
        //goi vao DB de insert hang loat ban ghi like
        unlikeMapData.forEach((key, value) -> {
            commentLikeRepository.deleteAllByCommentIdAndUserId(Long.valueOf(value.getPostId()), Long.valueOf(value.getUserId()));
        });
        log.info("unlike comment job finished");
    }
}
