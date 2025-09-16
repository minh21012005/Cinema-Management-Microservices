package com.example.domain.response;

import com.example.domain.entity.RoleDTO;
import com.example.util.constant.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private Long id;
    private String email;
    private String name;
    private String role;
}
