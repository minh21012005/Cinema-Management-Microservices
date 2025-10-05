package com.example.domain.request;

import lombok.Data;

/**
 * Dữ liệu SePay gửi qua webhook (JSON → object)
 */
@Data
public class SePayWebhookDTO {

    private Long id; // ID giao dịch trên SePay
    private String gateway; // Tên ngân hàng (VD: Vietcombank)
    private String transactionDate; // "2023-03-25 14:02:37"
    private String accountNumber; // Số tài khoản ngân hàng
    private String code; // Mã code thanh toán (nếu có)
    private String content; // Nội dung chuyển khoản
    private String transferType; // "in" hoặc "out"
    private Double transferAmount; // Số tiền giao dịch
    private Double accumulated; // Số dư tài khoản sau giao dịch
    private String subAccount; // Tài khoản phụ (nếu có)
    private String referenceCode; // Mã tham chiếu SMS
    private String description; // Toàn bộ nội dung tin nhắn SMS
}
