package com.example.client;

import com.example.config.SecurityConfig;
import com.example.domain.response.ApiResponse;
import com.example.domain.response.MovieResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service", configuration = SecurityConfig.class)
public interface UserClient {

    @GetMapping("/api/v1/users/fetch-cinema")
    ApiResponse<Long> findCinemaIdByUser();
}
