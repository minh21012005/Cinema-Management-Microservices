package com.example.client;

import org.springframework.stereotype.Component;

@Component
class ShowtimeClientFallback implements ShowtimeClient {

    @Override
    public void disableShowtimesByMovie(Long id) {
        throw new RuntimeException("Không thể kết nối Showtime service, vui lòng thử lại sau");
    }
}