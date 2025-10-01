package com.example.client;

import com.example.domain.response.ApiResponse;
import org.springframework.stereotype.Component;

@Component
class UserClientFallback implements UserClient {

    @Override
    public ApiResponse<Long> findCinemaIdByUser() {
        throw new RuntimeException("Không thể kết nối User service, vui lòng thử lại sau");
    }
}