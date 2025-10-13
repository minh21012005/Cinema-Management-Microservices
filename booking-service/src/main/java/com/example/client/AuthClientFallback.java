package com.example.client;

import com.example.domain.response.ApiResponse;
import org.springframework.stereotype.Component;

@Component
class AuthClientFallback implements AuthClient {

    @Override
    public String fetchEmailById(Long id) {
        throw new RuntimeException("Không thể kết nối Auth service, vui lòng thử lại sau");
    }
}