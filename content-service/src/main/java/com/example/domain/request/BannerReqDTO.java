package com.example.domain.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerReqDTO {

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 150, message = "Tiêu đề tối đa 150 ký tự")
    private String title;

    @Size(max = 300, message = "Subtitle tối đa 300 ký tự")
    private String subtitle;

    @NotBlank(message = "Ảnh banner không được để trống")
    private String imageUrl;

    private String redirectUrl; // link đến trang phim / event / external

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Min(value = 0, message = "Thứ tự hiển thị không được âm")
    private int displayOrder;

    private boolean active = true;
}

