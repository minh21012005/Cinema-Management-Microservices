package com.example.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDTO {
    @NotBlank
    private String name;

    private LocalDate dateOfBirth;

    private String gender;

    private String phone;

    private String address;

    // quản trị
    @NotNull
    private Long roleId;   // id role trong auth-service

    private boolean enabled;  // bật/tắt tài khoản

    // Có thể cho phép đổi email nếu business yêu cầu
    private String email;
}
