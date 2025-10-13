package com.example.client;

import com.example.config.SecurityConfig;
import com.example.domain.entity.TicketEmailDTO;
import com.example.domain.request.TicketDataRequest;
import com.example.domain.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", configuration = SecurityConfig.class)
public interface AuthClient {

    @GetMapping("/api/v1/users/{id}/email")
    String fetchEmailById(@PathVariable("id") Long id);
}
