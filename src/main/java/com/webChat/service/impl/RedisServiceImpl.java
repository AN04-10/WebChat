package com.webChat.service.impl;

import com.webChat.manager.RedisManager;
import com.webChat.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisManager redisManager;

    @Override
    public void setValue(String key, Object value) {
        redisManager.setValue(key, value);
    }

    @Override
    public Object getValue(String key) {
        return redisManager.getValue(key);
    }

    @Override
    public Boolean delete(String key) {
        return redisManager.delete(key);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisManager.expire(key, timeout, unit);
    }

    @Override
    public Boolean setValue(String key, String item, Object value) {
        return redisManager.hset(key, item, value);
    }

    @Override
    public Object getValue(String key, String item) {
        return redisManager.hget(key, item);
    }

    @Override
    public Boolean delete(String key, Object... item) {
        return redisManager.hdel(key, item);
    }


}
