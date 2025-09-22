package com.example.domain.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowtimeResDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean active;
    private Long movieId;   // lấy trực tiếp
    private String movieTitle;  // thêm tên phim
    private Long roomId;    // chỉ lấy id thay vì map nguyên Room
    private String roomName;    // thêm tên phòng
}
