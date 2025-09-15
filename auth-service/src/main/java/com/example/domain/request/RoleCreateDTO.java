package com.example.domain.request;

import com.example.validator.UniqueRoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RoleCreateDTO {

    @NotBlank(message = "Role name is required")
    @Size(min = 3, max = 50, message = "Role name must be between 3 and 50 characters")
    @UniqueRoleName
    private String name;

    private String description;

    private boolean active = true;

    private List<Long> permissionIds; // chỉ truyền id của permission
}
