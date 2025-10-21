package com.example.client;

import com.example.config.SecurityConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", configuration = SecurityConfig.class)
public interface AuthClient {

    @GetMapping("/api/v1/users/{id}/email")
    String fetchEmailById(@PathVariable("id") Long id);
}
