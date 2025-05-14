package com.example.noti_service.kafka;

import com.example.noti_service.component.JsonFactory;
import com.example.noti_service.dto.LikeOrUnLikeDTO;
import com.example.noti_service.dto.NotiCommentDTO;
import com.google.gson.reflect.TypeToken;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.*;

@Service
@Transactional
public class KafkaConsumer {
    public static Map<String, List<LikeOrUnLikeDTO>> likePostMap = new HashMap<>();
    public static Map<String, List<LikeOrUnLikeDTO>> unlikePostMap = new HashMap<>();
    public static Map<String, List<LikeOrUnLikeDTO>> likeCommentMap = new HashMap<>();
    public static Map<String, List<LikeOrUnLikeDTO>> unlikeCommentMap = new HashMap<>();
    public static Map<String, List<NotiCommentDTO>> commentMap = new HashMap<>();

    @KafkaListener(topics = "like-or-unlike-post-notification", groupId = "noti-group")
    public void listenLikeOrUnlikePost(String message) {
        Type type = new TypeToken<List<LikeOrUnLikeDTO>>() {}.getType();
        List<LikeOrUnLikeDTO> listMsg = JsonFactory.fromJson(message, type);
        listMsg.forEach(dto -> {
            if (dto.getUserId().equals(dto.getRecipientId())) {
                return;
            }
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
        });
    }

    @KafkaListener(topics = "cmt-post-notification", groupId = "noti-group")
    public void listenCommentPost(String message) {
        NotiCommentDTO dto = JsonFactory.fromJson(message, NotiCommentDTO.class);
        List<NotiCommentDTO> listComment = commentMap.computeIfAbsent(dto.getPostId(), k -> new ArrayList<>());
        listComment.add(dto);
    }
}
