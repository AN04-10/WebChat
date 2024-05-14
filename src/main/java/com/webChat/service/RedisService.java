package com.webChat.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    void setValue(String key, Object value);

    Object getValue(String key);

    Boolean delete(String key);

    Boolean expire(String key, long timeout, TimeUnit unit);

    /**
     * hset
     * @param key
     * @param item
     * @param value
     * @return
     */
    Boolean setValue(String key,String item,Object value);

    /**
     * hget
     * @param key
     * @param item
     * @return
     */
    Object getValue(String key,String item);

    /**
     * hdel
     * @param key
     * @param item
     * @return
     */
    Boolean delete(String key, Object... item);

}
