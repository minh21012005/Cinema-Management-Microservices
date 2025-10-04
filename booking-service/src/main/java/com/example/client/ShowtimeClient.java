package com.example.client;

import com.example.config.SecurityConfig;
import com.example.domain.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "cinema-service", configuration = SecurityConfig.class)
public interface ShowtimeClient {

    @GetMapping("/api/v1/showtime/{id}/is-end")
    boolean isShowtimeEnd(@PathVariable("id") Long id);
}
