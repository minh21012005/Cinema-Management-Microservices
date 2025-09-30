package com.example.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PermissionReqDTO {
    @NotBlank(message = "Code is required")
    @Size(min = 2, max = 50, message = "Code must be between 2 and 50 characters")
    private String code;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Method is required")
    private String method;

    @NotBlank(message = "API Path is required")
    private String apiPath;

    @NotBlank(message = "Module is required")
    private String module;

    private String description; // optional
    private boolean active;
}
