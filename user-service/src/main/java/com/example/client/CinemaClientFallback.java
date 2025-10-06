package com.example.client;

import org.springframework.stereotype.Component;

@Component
class CinemaClientFallback implements CinemaClient {

    @Override
    public boolean isCinemaExists(Long id) {
        throw new RuntimeException("Không thể kết nối Cinema service, vui lòng thử lại sau");
    }
}