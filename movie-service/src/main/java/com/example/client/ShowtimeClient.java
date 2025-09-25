package com.example.client;

import com.example.config.SecurityConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "cinema-service", configuration = SecurityConfig.class)
public interface ShowtimeClient {

    @PutMapping("/api/v1/showtime/disable-by-movie/{id}")
    void disableShowtimesByMovie(@PathVariable("id") Long id);

}
