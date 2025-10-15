package com.example.client;

import org.springframework.stereotype.Component;

@Component
class UserClientFallback implements UserClient {

    @Override
    public String getNameByEmail(String email) {
        throw new RuntimeException("Không thể kết nối User service, vui lòng thử lại sau");
    }
}