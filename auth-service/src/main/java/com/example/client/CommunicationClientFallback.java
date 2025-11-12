package com.example.client;

import org.springframework.stereotype.Component;

@Component
class CommunicationClientFallback implements CommunicationClient {

    @Override
    public Void sendOtpEmail(String email, String otp) {
        throw new RuntimeException("Không thể kết nối Communication service, vui lòng thử lại sau");
    }
}