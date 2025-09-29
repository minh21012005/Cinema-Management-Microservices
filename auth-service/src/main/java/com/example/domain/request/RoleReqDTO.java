package com.example.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RoleReqDTO {

    @NotBlank(message = "Role name is required")
    @Size(min = 3, max = 50, message = "Role name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Role code is required")
    @Size(min = 2, max = 20, message = "Role code must be between 2 and 20 characters")
    private String code;

    private String description;

    private boolean active = true;

    private List<Long> permissionIds; // chỉ truyền id của permission
}
