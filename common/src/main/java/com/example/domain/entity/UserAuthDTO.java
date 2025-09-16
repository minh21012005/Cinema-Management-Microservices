package com.example.domain.entity;

import lombok.Data;

@Data
public class UserAuthDTO {
    private String email;
    private String password;
    private Long roleId;
}
