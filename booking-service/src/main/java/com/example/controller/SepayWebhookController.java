package com.example.controller;

import com.example.domain.response.SepayWebhookPayload;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sepay")
public class SepayWebhookController {
    private final OrderService orderService;

    public SepayWebhookController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Value("${sepay.api-key}")
    private String apiKey;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody SepayWebhookPayload payload) {

        // ✅ 1. Kiểm tra xác thực bằng API Key
        if (authHeader == null || !authHeader.equals("Apikey " + apiKey)) {
            return ResponseEntity.status(403).body("Unauthorized");
        }

        // ✅ 2. Kiểm tra giao dịch là tiền vào
        if (!"in".equalsIgnoreCase(payload.getTransferType())) {
            return ResponseEntity.ok("Ignored non-incoming transaction");
        }

        System.out.println("📩 Webhook received: " + payload);

        // TODO: cập nhật trạng thái giao dịch trong DB (đã thanh toán/thất bại)
        return ResponseEntity.ok("Received");
    }
}
