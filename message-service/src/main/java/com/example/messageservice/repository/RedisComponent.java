package com.example.messageservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisComponent implements IRedisComponent {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    @Override
    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void saveWithExpiration(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, DEFAULT_TIME_UNIT);
    }

    @Override
    public void saveWithExpiration(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    @Override
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public String getAndRefresh(String key, long timeout) {
        String value = get(key);
        if (value != null) {
            redisTemplate.expire(key, timeout, DEFAULT_TIME_UNIT);
        }
        return value;
    }

    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Long getExpiration(String key) {
        return redisTemplate.getExpire(key, DEFAULT_TIME_UNIT);
    }

    @Override
    public boolean refresh(String key, long timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, DEFAULT_TIME_UNIT));
    }
}
