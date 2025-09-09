package com.example.client;

import org.springframework.stereotype.Component;

@Component
class UserClientFallback implements UserClient {
    @Override
    public Boolean isPhoneExist(String phone) {
        // fallback strategy: báo lỗi hoặc trả false
        throw new RuntimeException("Không thể kết nối User service, vui lòng thử lại sau");
    }
}