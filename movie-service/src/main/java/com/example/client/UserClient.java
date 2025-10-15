package com.example.client;

import com.example.config.SecurityConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", configuration = SecurityConfig.class)
public interface UserClient {

    @GetMapping("/api/v1/users/fetch-name-by-email")
    String getNameByEmail(@RequestParam("email") String email);

}
