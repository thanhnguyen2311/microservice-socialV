package com.example.noti_service.kafka;

import com.example.noti_service.component.JsonFactory;
import com.example.noti_service.dto.LikeOrUnLikeDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class KafkaConsumer {
    public static Map<String, List<LikeOrUnLikeDTO>> likePostMap = new HashMap<>();
    public static Map<String, List<LikeOrUnLikeDTO>> unlikePostMap = new HashMap<>();
    public static Map<String, List<LikeOrUnLikeDTO>> likeCommentMap = new HashMap<>();
    public static Map<String, List<LikeOrUnLikeDTO>> unlikeCommentMap = new HashMap<>();

    @KafkaListener(topics = "like-or-unlike-post", groupId = "noti-group")
    public void listenLikeOrUnlikePost(String message) {
        LikeOrUnLikeDTO dto = JsonFactory.fromJson(message, LikeOrUnLikeDTO.class);
        //gom nhóm theo post
        List<LikeOrUnLikeDTO> listUserLike = likePostMap.computeIfAbsent(dto.getPostId(), k -> new ArrayList<>());
        List<LikeOrUnLikeDTO> listUserUnlike = unlikePostMap.computeIfAbsent(dto.getPostId(), k -> new ArrayList<>());

        if ("1".equals(dto.getType())) { // Like
            if (!listUserUnlike.removeIf(user -> user.getUserId().equals(dto.getUserId()))) {
                listUserLike.add(dto);
            }
        } else { // Unlike
            if (!listUserLike.removeIf(user -> user.getUserId().equals(dto.getUserId()))) {
                listUserUnlike.add(dto);
            }
        }
    }

}
