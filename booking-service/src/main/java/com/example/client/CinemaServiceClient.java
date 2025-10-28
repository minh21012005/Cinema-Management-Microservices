package com.example.client;

import com.example.config.SecurityConfig;
import com.example.domain.entity.TicketEmailDTO;
import com.example.domain.request.TicketDataRequest;
import com.example.domain.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cinema-service", configuration = SecurityConfig.class)
public interface CinemaServiceClient {

    @GetMapping("/api/v1/showtime/{id}/is-end")
    boolean isShowtimeEnd(@PathVariable("id") Long id);

    @PostMapping("/api/v1/showtime/{id}/ticket-data")
    ApiResponse<TicketEmailDTO> fetchTicketData(
            @PathVariable("id") Long id,
            @RequestBody TicketDataRequest request);

    @GetMapping("/api/v1/seats/count-active")
    ApiResponse<Long> countActiveSeatsByMonth();
}
