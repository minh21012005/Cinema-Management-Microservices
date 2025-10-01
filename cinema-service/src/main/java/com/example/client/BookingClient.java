package com.example.client;

import com.example.config.SecurityConfig;
import com.example.domain.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "booking-service", configuration = SecurityConfig.class)
public interface BookingClient {

    @GetMapping("/api/v1/tickets/showtime/{id}/booked-seats")
    ApiResponse<List<Long>> getBookedSeatIds(@PathVariable("id") Long id);
}
