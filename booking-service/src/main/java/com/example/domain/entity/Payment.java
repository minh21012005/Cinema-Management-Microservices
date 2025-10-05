package com.example.domain.entity;

import com.example.domain.enums.PaymentMethod;
import com.example.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Loại phương thức: COD, SEPAY, PAYOS, v.v.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    // Số tiền thanh toán (thường = order.totalAmount)
    @Column(nullable = false)
    private Double amount;

    // Trạng thái: PENDING, SUCCESS, FAILED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    // Ghi chú thêm (ví dụ: “Thanh toán tiền mặt tại quầy”)
    private String note;

    // Nếu là giao dịch SePay → liên kết đến SePayTransaction
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sepay_tx_id")
    private SePayTransaction sePayTransaction;
}
