package com.example.domain.response;

import com.example.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private String phone;
    private LocalDate dateOfBirth;
    private Instant createdAt;
}
