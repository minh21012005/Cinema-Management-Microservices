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
public class Order extends BaseEntity<Long> {

    // Người mua (nếu có tài khoản)
    @Column(name = "user_id")
    private Long userId;

    // Nhân viên xử lý đơn này (nếu bán trực tiếp hoặc hỗ trợ online)
    @Column(name = "staff_id")
    private Long staffId;

    // Thông tin khách hàng (nếu cần lưu vãng lai hoặc override từ profile)
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
}
