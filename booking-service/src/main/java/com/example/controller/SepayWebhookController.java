package com.example.controller;

import com.example.domain.request.SepayWebhookReqDTO;
import com.example.domain.response.OrderResDTO;
import com.example.service.PaymentService;
import com.example.util.error.IdInvalidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sepay")
public class SepayWebhookController {
    private final PaymentService paymentService;

    public SepayWebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Value("${sepay.api-key}")
    private String apiKey;

    @Value("${sepay.account}")
    private String bankAccount;

    @Value("${sepay.bank}")
    private String bankCode;

    @PostMapping("/webhook")
    public ResponseEntity<OrderResDTO> handleWebhook(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody SepayWebhookReqDTO payload) throws IdInvalidException {

        // ✅ 1. Kiểm tra xác thực bằng API Key
        if (authHeader == null || !authHeader.equals("Apikey " + apiKey)) {
            throw new IdInvalidException("Unauthorized");
        }

        return ResponseEntity.ok(paymentService.confirmSepayPayment(payload));
    }

    @GetMapping("/generate-qr")
    public ResponseEntity<String> generateQr(
            @RequestParam("amount") Double amount,
            @RequestParam("orderId") Long orderId
            ) throws IdInvalidException {
        // ✅ Validate cơ bản
        if (amount <= 0) {
            throw new IdInvalidException("Số tiền không hợp lệ!");
        }

        String amountFormatted = String.format("%.2f", amount);

        // ✅ Ghép URL QR code SEPAY
        String qrUrl = String.format(
                "https://qr.sepay.vn/img?acc=%s&bank=%s&template=compact&amount=%s&des=%s",
                bankAccount, bankCode, amountFormatted, orderId
        );

        return ResponseEntity.ok(qrUrl);
    }
}
