package com.example.domain.response;

import lombok.Data;

@Data
public class CategoryResDTO {
    private Long id;
    private String name;
    private String code;
    private boolean active;
}
