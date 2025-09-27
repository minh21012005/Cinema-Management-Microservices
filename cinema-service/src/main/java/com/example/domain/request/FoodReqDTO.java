package com.example.domain.request;

import lombok.Data;

@Data
public class FoodReqDTO {
    private String code;       // Mã món ăn, VD: FD001
    private String name;       // Tên món ăn
    private Double price;      // Giá
    private String description;// Mô tả
    private Boolean available; // Có bán hay không
    private String imageKey;   // Key/URL ảnh
    private Long typeId;       // ID loại món ăn (FoodType)
}
