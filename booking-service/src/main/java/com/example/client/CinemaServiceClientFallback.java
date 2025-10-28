package com.example.client;

import com.example.domain.entity.TicketEmailDTO;
import com.example.domain.request.TicketDataRequest;
import com.example.domain.response.ApiResponse;
import org.springframework.stereotype.Component;

@Component
class CinemaServiceClientFallback implements CinemaServiceClient {

    @Override
    public boolean isShowtimeEnd(Long id) {
        throw new RuntimeException("Không thể kết nối Showtime service, vui lòng thử lại sau");
    }

    @Override
    public ApiResponse<TicketEmailDTO> fetchTicketData(Long id, TicketDataRequest request) {
        throw new RuntimeException("Không thể kết nối Showtime service, vui lòng thử lại sau");
    }

    @Override
    public ApiResponse<Long> countActiveSeatsByMonth() {
        throw new RuntimeException("Không thể kết nối Showtime service, vui lòng thử lại sau");
    }
}