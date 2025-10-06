package com.example.client;

import com.example.config.SecurityConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cinema-service", configuration = SecurityConfig.class)
public interface CinemaClient {
    @GetMapping("/api/v1/cinemas/{id}/exists")
    boolean isCinemaExists(@PathVariable("id") Long id);
}
