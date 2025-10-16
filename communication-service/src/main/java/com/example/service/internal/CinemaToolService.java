package com.example.service.internal;

import com.example.client.MovieClient;
import com.example.domain.response.MovieResDTO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaToolService {

    private final MovieClient movieClient; // optional: Feign client to movie-service

    @Tool(description = "Lấy danh sách phim đang chiếu (no args).")
    public String getNowPlayingMovies() {
        // Example calling internal movie-service; fallback to hardcoded if client missing
        try {
            List<MovieResDTO> movies = movieClient.getNowShowing(100).getData();
            if (movies == null || movies.isEmpty()) {
                return "Hiện tại chưa có phim nào đang chiếu.";
            }
            return "Các phim đang chiếu: " +
                    movies.stream().map(MovieResDTO::getTitle).collect(Collectors.joining(", "));
        } catch (Exception ex) {
            // fallback simple reply
            return "Không thể lấy danh sách phim hiện tại. Vui lòng thử lại sau.";
        }
    }

    @Tool(description = "Hiển thị menu combo đồ ăn tại rạp.")
    public String getFoodMenu() {
        // call internal service if any
        return "Combo A: Bắp nhỏ + Nước nhỏ; Combo B: Bắp lớn + Nước lớn; Giá tham khảo: 70k - 150k.";
    }

    @Tool(description = "Hướng dẫn cách đặt vé.")
    public String getBookingGuide() {
        return "Để đặt vé: chọn phim -> chọn suất -> chọn ghế -> thanh toán. Hệ thống gửi mã vé vào email/SMS.";
    }
}
