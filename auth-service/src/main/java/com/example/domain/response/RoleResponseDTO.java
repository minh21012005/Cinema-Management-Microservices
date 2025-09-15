package com.example.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDTO {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private List<String> permissions; // trả về danh sách tên permission
}

