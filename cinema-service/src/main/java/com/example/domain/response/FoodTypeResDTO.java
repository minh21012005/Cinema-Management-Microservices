package com.example.domain.response;

import lombok.Data;

@Data
public class FoodTypeResDTO {
    private Long id;          // ID loại món ăn
    private String name;      // Tên loại món ăn, VD: Đồ uống, Bắp, Snack
    private String description;// Mô tả
    private String code;      // Mã loại món ăn, VD: DRINK, POPCORN
    private boolean active;    // Trạng thái loại món ăn (còn sử dụng hay không)
}
