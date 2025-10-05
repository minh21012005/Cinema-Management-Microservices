package com.example.domain.request;

import com.example.domain.enums.PaymentMethod;
import com.example.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dùng để tạo hoặc cập nhật thông tin thanh toán.
 * - Áp dụng cho cả COD (staff xác nhận) và thanh toán online (webhook).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentReqDTO {

    // ID của đơn hàng cần thanh toán
    private Long orderId;

    // Phương thức thanh toán: COD, SEPAY, PAYOS, v.v.
    private PaymentMethod method;

    // Số tiền thanh toán (thường = order.totalAmount)
    private Double amount;

    // Trạng thái thanh toán: PENDING, SUCCESS, FAILED
    private PaymentStatus status;

    // Ghi chú thêm (nếu cần)
    private String note;

    // Dành riêng cho các cổng thanh toán (ví dụ SePay / PayOS)
    private String transactionId;   // Mã giao dịch từ phía cổng (nếu có)
    private String referenceCode;   // Mã tham chiếu / nội dung chuyển khoản
}
