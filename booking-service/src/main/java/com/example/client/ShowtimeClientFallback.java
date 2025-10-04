package com.example.client;

import com.example.domain.response.ApiResponse;
import org.springframework.stereotype.Component;

@Component
class ShowtimeClientFallback implements ShowtimeClient {

    @Override
    public boolean isShowtimeEnd(Long id) {
        throw new RuntimeException("Không thể kết nối Showtime service, vui lòng thử lại sau");
    }
}