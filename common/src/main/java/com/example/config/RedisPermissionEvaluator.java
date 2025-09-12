package com.example.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class RedisPermissionEvaluator implements PermissionEvaluator {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisPermissionEvaluator(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if (auth == null || permission == null) return false;

        String userId = auth.getName(); // sub từ JWT
        String redisKey = "user:permissions:" + userId;
        Object val = redisTemplate.opsForHash().get(redisKey, permission.toString());
        return "1".equals(val != null ? val.toString() : null);
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        return hasPermission(auth, null, permission);
    }
}

