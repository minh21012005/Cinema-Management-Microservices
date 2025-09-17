package com.example.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDTO {
    @NotBlank
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    @NotBlank
    private String phone;
    private String address;
}
