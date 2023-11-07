package com.crmfoodestablishment.userauthservice.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.UUID;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<UUID, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<UUID, String> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(UUID.class));
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
