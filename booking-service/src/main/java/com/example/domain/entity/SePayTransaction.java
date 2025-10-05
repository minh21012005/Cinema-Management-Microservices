package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "sepay_transactions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"sepay_id"}) // tránh trùng id giao dịch
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SePayTransaction extends BaseEntity<Long> {

    // ID giao dịch trên SePay (ví dụ: 92704)
    @Column(name = "sepay_id", nullable = false, unique = true)
    private Long sepayId;

    // Tên ngân hàng (VD: Vietcombank)
    @Column(name = "gateway")
    private String gateway;

    // Ngày giờ giao dịch (transactionDate)
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    // Số tài khoản ngân hàng
    @Column(name = "account_number")
    private String accountNumber;

    // Mã code thanh toán (nếu có)
    @Column(name = "code")
    private String code;

    // Nội dung chuyển khoản
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    // Loại giao dịch: in / out
    @Column(name = "transfer_type")
    private String transferType;

    // Số tiền giao dịch
    @Column(name = "transfer_amount", nullable = false)
    private Double transferAmount;

    // Số dư tài khoản sau giao dịch (accumulated)
    @Column(name = "accumulated")
    private Double accumulated;

    // Tài khoản phụ (nếu có)
    @Column(name = "sub_account")
    private String subAccount;

    // Mã tham chiếu (referenceCode)
    @Column(name = "reference_code")
    private String referenceCode;

    // Toàn bộ nội dung tin nhắn SMS
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Thời điểm hệ thống của bạn nhận webhook
    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    // Trạng thái giao dịch (SUCCESS/FAILED/PENDING)
    @Column(name = "status", nullable = false)
    private String status;

    // Lưu bản JSON gốc để đối chiếu/debug
    @Lob
    @Column(name = "raw_data", columnDefinition = "LONGTEXT")
    private String rawData;

    // Gắn với đơn hàng nếu có thể xác định
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // Liên kết ngược sang Payment (nếu thanh toán thành công)
    @OneToOne(mappedBy = "sePayTransaction")
    private Payment payment;
}
