package com.example.client;

import org.springframework.stereotype.Component;

@Component
class AuthClientFallback implements AuthClient {

    @Override
    public String getRoleCode(Long id) {
        throw new RuntimeException("Không thể kết nối Auth service, vui lòng thử lại sau");
    }

    @Override
    public boolean isUserEnabled(String email) {
        throw new RuntimeException("Không thể kết nối Auth service, vui lòng thử lại sau");
    }
}