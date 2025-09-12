package com.example.controller;

import com.example.domain.User;
import com.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.interceptor.RequiredPermission;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController<User, Long>{

    private final UserService userService;

    public UserController(UserService service) {
        super(service);
        this.userService = service;
    }

    @GetMapping("/check-phone")
    public Boolean checkPhone(@RequestParam("phone") String phone) {
        return userService.isPhoneExist(phone);
    }

    @GetMapping("test-permission")
    @RequiredPermission("BOOKING_MANAGE")
    public ResponseEntity<String> getPosts() {
        return ResponseEntity.ok("BOOKING_MANAGE");
    }

    @PostMapping("test-permission")
    @RequiredPermission("BOOKING_VIEW")
    public ResponseEntity<String> createPost() {
        return ResponseEntity.ok("Post created");
    }

    @GetMapping("test")
    @PreAuthorize("hasPermission(null, 'BOOKING_MANAGE')")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("test created");
    }
}
