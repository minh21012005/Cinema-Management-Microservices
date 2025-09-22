package com.example.client;

import com.example.domain.response.ApiResponse;
import com.example.domain.response.MovieResDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class MovieClientFallback implements MovieClient {

    @Override
    public ApiResponse<MovieResDTO> findById(Long id) {
        throw new RuntimeException("Không thể kết nối Movie service, vui lòng thử lại sau");
    }

    @Override
    public ApiResponse<List<MovieResDTO>> findByIds(List<Long> id) {
        throw new RuntimeException("Không thể kết nối Movie service, vui lòng thử lại sau");
    }
}