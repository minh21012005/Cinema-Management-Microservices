package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity<Long>{
    // Người mua
    @Column(name = "user_id")
    private Long userId;

    // Tổng tiền
    @Column(nullable = false)
    private Double totalAmount;

    // Trạng thái đơn hàng
    @Column(nullable = false)
    private boolean paid = false; // true = đã thanh toán, false = chưa

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

}
