package com.example.entity;

import com.example.domain.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends BaseEntity<Long> {
    @Column(nullable = false, unique = true)
    private String code; // MOVIE_CREATE, BOOKING_MANAGE...

    @Column(nullable = false)
    private String name; // Hiển thị: "Tạo phim", "Quản lý đặt vé"

    private String description; // optional
}
