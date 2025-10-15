package com.example.domain.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateProfileDTO {
    private String name;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String address;
    private Long roleId;
    private String oldEmail;
    private Long authId;
}
