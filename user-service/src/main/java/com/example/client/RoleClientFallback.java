package com.example.client;

import org.springframework.stereotype.Component;

@Component
class RoleClientFallback implements RoleClient {

    @Override
    public String getRoleCode(Long id) {
        throw new RuntimeException("Không thể kết nối Auth service, vui lòng thử lại sau");
    }
}