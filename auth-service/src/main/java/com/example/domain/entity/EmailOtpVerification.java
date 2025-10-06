package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_otp_verification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailOtpVerification extends BaseEntity<Long>{
    @Column(nullable = false, unique = false, length = 150)
    private String email;

    @Column(nullable = false, length = 10)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private boolean verified = false;

    // Lưu JSON thông tin người dùng đăng ký (name, password, phone...)
    @Lob
    @Column(columnDefinition = "TEXT")
    private String rawData;
}
