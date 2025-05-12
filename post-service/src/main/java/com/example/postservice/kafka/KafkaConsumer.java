package com.example.postservice.kafka;

import com.example.postservice.component.JsonFactory;
import com.example.postservice.dto.LikeOrUnLikeDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaConsumer {

    public static Map<String, LikeOrUnLikeDTO> likePostMap = new HashMap<>();
    public static Map<String, LikeOrUnLikeDTO> unlikePostMap = new HashMap<>();

    public static Map<String, LikeOrUnLikeDTO> likeCommentMap = new HashMap<>();
    public static Map<String, LikeOrUnLikeDTO> unlikeCommentMap = new HashMap<>();

    @KafkaListener(topics = "like-or-unlike-post", groupId = "post-group")
    public void listenLikeOrUnlikePost(String message) {
        LikeOrUnLikeDTO dto = JsonFactory.fromJson(message, LikeOrUnLikeDTO.class);
        if (dto.getType().equals("1")) {
            if (unlikePostMap.containsKey(dto.getUserId() + "_" + dto.getPostId())) {
                unlikePostMap.remove(dto.getUserId() + "_" + dto.getPostId());
            } else {
                likePostMap.put(dto.getUserId() + "_" + dto.getPostId(), dto);
            }
        } else {
            if (likePostMap.containsKey(dto.getUserId() + "_" + dto.getPostId())) {
                likePostMap.remove(dto.getUserId() + "_" + dto.getPostId());
            } else {
                unlikePostMap.put(dto.getUserId() + "_" + dto.getPostId(), dto);
            }
        }
    }

    @KafkaListener(topics = "like-or-unlike-comment", groupId = "post-group")
    public void listenLikeOrUnlikeComment(String message) {
        LikeOrUnLikeDTO dto = JsonFactory.fromJson(message, LikeOrUnLikeDTO.class);
        if (dto.getType().equals("1")) {
            if (unlikeCommentMap.containsKey(dto.getUserId() + "_" + dto.getCommentId())) {
                unlikeCommentMap.remove(dto.getUserId() + "_" + dto.getCommentId());
            } else {
                likeCommentMap.put(dto.getUserId() + "_" + dto.getCommentId(), dto);
            }
        } else {
            if (likeCommentMap.containsKey(dto.getUserId() + "_" + dto.getCommentId())) {
                likeCommentMap.remove(dto.getUserId() + "_" + dto.getCommentId());
            } else {
                unlikeCommentMap.put(dto.getUserId() + "_" + dto.getCommentId(), dto);
            }
        }
    }
}
