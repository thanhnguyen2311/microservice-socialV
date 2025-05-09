package com.example.postservice.kafka;

import com.example.postservice.component.JsonFactory;
import com.example.postservice.dto.LikeOrUnLikePostDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaConsumer {

    public static Map<String, LikeOrUnLikePostDTO> likeMap = new HashMap<>();
    public static Map<String, LikeOrUnLikePostDTO> unlikeMap = new HashMap<>();

    @KafkaListener(topics = "like-or-unlike-post", groupId = "post-group")
    public void listen(String message) {
        LikeOrUnLikePostDTO dto = JsonFactory.fromJson(message, LikeOrUnLikePostDTO.class);
        if (dto.getType().equals("1")) {
            if (unlikeMap.containsKey(dto.getUserId() + "_" + dto.getPostId())) {
                unlikeMap.remove(dto.getUserId() + "_" + dto.getPostId());
            } else {
                likeMap.put(dto.getUserId() + "_" + dto.getPostId(), dto);
            }
        } else {
            if (likeMap.containsKey(dto.getUserId() + "_" + dto.getPostId())) {
                likeMap.remove(dto.getUserId() + "_" + dto.getPostId());
            } else {
                unlikeMap.put(dto.getUserId() + "_" + dto.getPostId(), dto);
            }
        }
    }
}
