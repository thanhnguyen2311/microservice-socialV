package com.example.postservice.repository;


import java.util.concurrent.TimeUnit;

public interface IRedisComponent {
    public void save(String key, String value);
    public void saveWithExpiration(String key, Object value, long timeout);
    public void saveWithExpiration(String key, Object value, long timeout, TimeUnit unit);
    public String get(String key);
    public String getAndRefresh(String key, long timeout);
    public boolean exists(String key);
    public void delete(String key);
    public Long getExpiration(String key);
    public boolean refresh(String key, long timeout);
}
