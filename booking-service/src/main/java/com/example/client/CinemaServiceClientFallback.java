package com.example.client;

import com.example.domain.entity.TicketEmailDTO;
import com.example.domain.request.TicketDataRequest;
import com.example.domain.response.ApiResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Component
class CinemaServiceClientFallback implements CinemaServiceClient {

    @Override
    public boolean isShowtimeEnd(Long id) {
        throw new RuntimeException("Không thể kết nối Cinema service, vui lòng thử lại sau");
    }

    @Override
    public ApiResponse<TicketEmailDTO> fetchTicketData(Long id, TicketDataRequest request) {
        throw new RuntimeException("Không thể kết nối Cinema service, vui lòng thử lại sau");
    }

    @Override
    public ApiResponse<Long> countActiveSeatsByMonth() {
        throw new RuntimeException("Không thể kết nối Cinema service, vui lòng thử lại sau");
    }

    @Override
    public ApiResponse<Map<String, Double>> getTopMovie(@RequestBody Map<Long, Double> showtimeToRevenue) {
        throw new RuntimeException("Không thể kết nối Cinema service, vui lòng thử lại sau");
    }
}