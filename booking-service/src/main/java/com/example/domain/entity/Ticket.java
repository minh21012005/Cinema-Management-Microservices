package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket extends BaseEntity<Long>{

    @Column(nullable = false)
    private double price; // Giá vé

    @Column(nullable = false)
    private boolean paid = false; // Trạng thái thanh toán

    @Column(nullable = false)
    private boolean reserved = false; // Tạm giữ ghế

    @Column(nullable = false)
    @Builder.Default
    private boolean used = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean valid = true;

    private LocalDateTime reservedAt; // Thời điểm bắt đầu giữ ghế

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @Column(name = "showtime_id", nullable = false)
    private Long showtimeId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
