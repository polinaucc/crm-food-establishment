package com.crmfoodestablishment.user_auth_service.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;

@Configuration
public class AuthConfig {

    @Bean
    public RedisTemplate<UUID, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<UUID, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
