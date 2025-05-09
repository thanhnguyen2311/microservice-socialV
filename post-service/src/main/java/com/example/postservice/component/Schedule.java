package com.example.postservice.component;

import com.example.postservice.dto.LikeOrUnLikePostDTO;
import com.example.postservice.entity.PostLike;
import com.example.postservice.repository.IPostLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.postservice.kafka.KafkaConsumer.likeMap;
import static com.example.postservice.kafka.KafkaConsumer.unlikeMap;

@Component
@Slf4j
public class Schedule {
    @Autowired
    private IPostLikeRepository postLikeRepository;

    @Scheduled(fixedRate = 2000) // 2s
    public void likeJob() {
        Map<String, LikeOrUnLikePostDTO> likeMapData = new HashMap<>(likeMap);
        likeMap.clear();
        log.info("like job started");
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
        log.info("like job finished");
    }

    @Scheduled(fixedRate = 2000) // 2s
    public void unlikeJob() {
        Map<String, LikeOrUnLikePostDTO> likeMapData = new HashMap<>(unlikeMap);
        unlikeMap.clear();
        log.info("unlike job started");
        //goi vao DB de insert hang loat ban ghi like
        log.info("unlike job finished");
    }
}
