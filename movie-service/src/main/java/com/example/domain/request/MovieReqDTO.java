package com.example.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieReqDTO {
    private String title; // Tên phim
    private String description; // Mô tả
    private int durationInMinutes; // Thời lượng
    private LocalDate releaseDate; // Ngày phát hành
    private LocalDate endDate; // Ngày kết thúc chiếu
    private String posterKey; // Poster key
    private String trailerUrl; // Link trailer YouTube
    private List<Long> categoryIds; // danh sách category id (hoặc có thể dùng List<String> categoryCodes)
}
