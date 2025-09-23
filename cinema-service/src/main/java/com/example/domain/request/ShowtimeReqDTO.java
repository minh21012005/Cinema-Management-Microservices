package com.example.domain.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowtimeReqDTO {
    private Long roomId;         // Phòng chiếu cụ thể
    private Long movieId;        // Phim chiếu (lấy từ movie-service)
    private LocalDateTime startTime;  // Thời gian bắt đầu
}
