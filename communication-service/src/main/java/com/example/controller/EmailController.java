package com.example.controller;

import com.example.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-otp")
    public ResponseEntity<Void> sendOtpEmail(@RequestParam("email") String email, @RequestParam("otp") String otp) {
        emailService.sendOtpEmail(email, otp);
        return ResponseEntity.ok(null);
    }
}
