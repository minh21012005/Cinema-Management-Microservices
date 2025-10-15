package com.example.controller;

import com.example.service.AiTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiTestController {

    private final AiTestService aiTestService;

    public AiTestController(AiTestService aiTestService) {
        this.aiTestService = aiTestService;
    }

    @GetMapping("/test-ai")
    public String testAi() {
        return aiTestService.test();
    }
}
