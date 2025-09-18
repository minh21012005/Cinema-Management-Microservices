package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/api/v1/roles/code")
    String getRoleCode(@RequestParam("id") Long id);

    @GetMapping("/api/v1/users/enabled")
    boolean isUserEnabled(@RequestParam("email") String email);
}
