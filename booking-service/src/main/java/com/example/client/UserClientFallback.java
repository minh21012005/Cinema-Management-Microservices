package com.example.client;

import com.example.domain.response.ApiResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
class UserClientFallback implements UserClient {

    @Override
    public String getNameByEmail(String email) {
        throw new RuntimeException("Không thể kết nối User service, vui lòng thử lại sau");
    }

    @Override
    public ApiResponse<Map<Long, String>> getNamesByIds(List<Long> ids) {
        throw new RuntimeException("Không thể kết nối User service, vui lòng thử lại sau");
    }
}