package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "tickets",
        uniqueConstraints = @UniqueConstraint(columnNames = {"seat_id", "showtime_id"})
)
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

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @Column(name = "showtime_id", nullable = false)
    private Long showtimeId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
