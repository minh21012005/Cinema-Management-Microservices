package com.example.domain.response;

import lombok.Data;

@Data
public class FoodResDTO {
    private Long id;
    private String code;       // Mã món ăn, VD: FD001
    private String name;       // Tên món ăn
    private Double price;      // Giá
    private String description;// Mô tả
    private Boolean available; // Có bán hay không
    private String imageKey;   // Key/URL ảnh
    private String typeName;
}
