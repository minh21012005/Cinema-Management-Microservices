package com.example.service.impl;

import com.example.client.CommunicationClient;
import com.example.domain.entity.EmailOtpVerification;
import com.example.domain.request.CreateUserRequest;
import com.example.repository.EmailOtpVerificationRepository;
import com.example.service.EmailOtpVerificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Service
public class EmailOtpVerificationServiceImpl implements EmailOtpVerificationService {
    private final EmailOtpVerificationRepository emailOtpVerificationRepository;
    private final ObjectMapper objectMapper;
    private final CommunicationClient communicationClient;

    public EmailOtpVerificationServiceImpl(EmailOtpVerificationRepository emailOtpVerificationRepository,
                                           ObjectMapper objectMapper, CommunicationClient communicationClient) {
        this.emailOtpVerificationRepository = emailOtpVerificationRepository;
        this.objectMapper = objectMapper;
        this.communicationClient = communicationClient;
    }

    @Override
    public ResponseEntity<?> sendOtp(CreateUserRequest req) throws JsonProcessingException {
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        String rawJson = objectMapper.writeValueAsString(req);

        EmailOtpVerification otpRecord = EmailOtpVerification.builder()
                .email(req.getEmail())
                .otp(otp)
                .expiredAt(expiredAt)
                .verified(false)
                .rawData(rawJson)
                .build();

        emailOtpVerificationRepository.save(otpRecord);

        communicationClient.sendOtpEmail(req.getEmail(), otp);

        return ResponseEntity.ok(Map.of(
                "message", "Mã OTP đã được gửi đến email của bạn",
                "expiredAt", expiredAt
        ));
    }

    @Scheduled(cron = "0 0 * * * *")
    public void cleanExpiredOtp() {
        LocalDateTime now = LocalDateTime.now();
        emailOtpVerificationRepository.deleteAllExpiredOrVerified(now);
    }
}
