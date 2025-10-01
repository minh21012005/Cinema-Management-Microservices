package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "food_id")
    private Long foodId; // nếu mua món ăn riêng

    @Column(name = "combo_id")
    private Long comboId; // nếu mua combo

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price; // tổng = quantity * đơn giá
}

