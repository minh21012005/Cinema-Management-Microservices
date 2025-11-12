package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "communication-service")
public interface CommunicationClient {

    @PostMapping("/api/v1/email/send-otp")
    Void sendOtpEmail(@RequestParam("email") String email, @RequestParam("otp") String otp);
}
