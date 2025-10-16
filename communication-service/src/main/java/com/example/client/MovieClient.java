package com.example.client;

import com.example.config.SecurityConfig;
import com.example.domain.response.ApiResponse;
import com.example.domain.response.MovieResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "movie-service", configuration = SecurityConfig.class)
public interface MovieClient {

    @GetMapping("/api/v1/movies/showing")
    ApiResponse<List<MovieResDTO>> getNowShowing(@RequestParam(value = "limit", defaultValue = "100") int limit);

}
