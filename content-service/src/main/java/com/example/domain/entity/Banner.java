package com.example.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "banners",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"display_order", "active"})}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner extends BaseEntity<Long> {

    @Column(nullable = false, length = 150)
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @Column(length = 300)
    private String subtitle;

    @NotBlank(message = "Ảnh banner không được để trống")
    @Column(nullable = false)
    private String imageKey;

    @Column(name = "redirect_url")
    private String redirectUrl; // link đến trang phim / event / external

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "display_order")
    @Min(value = 0, message = "Thứ tự hiển thị không được âm")
    private int displayOrder;

    @Column(nullable = false)
    private boolean active = true;
}
