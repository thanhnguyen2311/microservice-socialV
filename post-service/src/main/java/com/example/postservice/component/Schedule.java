package com.example.postservice.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.example.postservice.kafka.KafkaConsumer.likeMap;

@Component
@Slf4j
public class Schedule {

    @Scheduled(fixedRate = 2000) // 2s
    public void likeJob() {
        Map<String, String> likeMapData = new HashMap<>(likeMap);
        likeMap.clear();
        log.info("like job started");
        //goi vao DB de insert hang loat ban ghi like
        log.info("like job finished");
    }
}
