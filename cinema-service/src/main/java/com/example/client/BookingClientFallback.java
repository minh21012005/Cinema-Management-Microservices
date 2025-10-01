package com.example.client;

import com.example.domain.response.ApiResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
class BookingClientFallback implements BookingClient {

    @Override
    public ApiResponse<List<Long>> getBookedSeatIds(@PathVariable("id") Long id) {
        throw new RuntimeException("Không thể kết nối Booking service, vui lòng thử lại sau");
    }
}