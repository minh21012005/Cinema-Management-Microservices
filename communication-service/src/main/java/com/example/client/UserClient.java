package com.example.client;

import com.example.config.SecurityConfig;
import com.example.domain.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "user-service", configuration = SecurityConfig.class)
public interface UserClient {

    @GetMapping("/api/v1/users/fetch-name-by-email")
    String getNameByEmail(@RequestParam("email") String email);

    @GetMapping("/api/v1/users/fetch-name-by-ids")
    ApiResponse<Map<Long, String>> getNamesByIds(@RequestParam("ids") List<Long> ids);
}
