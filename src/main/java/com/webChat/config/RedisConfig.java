package com.webChat.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webChat.util.JSONUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;

@Configuration
public class RedisConfig {
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //springboot项目启动后，把redisTemplate这个Bean修改一下，修改一下key和value的序列化方式

        //设置key序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //对象映射工具，Java对象 和 json对象进行相互转化
        ObjectMapper mapper = new ObjectMapper();
        //设置可见性
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //激活类型
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.EVERYTHING);

        //设置value序列化
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(mapper, Object.class));

        //设置hashKey序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        //设置haskValue序列化
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(mapper, Object.class));
        return redisTemplate;
    }
}