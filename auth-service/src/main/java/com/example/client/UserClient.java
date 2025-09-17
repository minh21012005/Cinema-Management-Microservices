package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/v1/users/check-phone")
    Boolean isPhoneExist(@RequestParam("phone") String phone);

    @GetMapping("/api/v1/users/check-phone-update")
    Boolean isPhoneExist(@RequestParam("email") String email,
                         @RequestParam("newPhone") String newPhone);
}
