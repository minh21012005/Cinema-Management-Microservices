package com.example.controller;

import com.example.domain.User;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

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
}
