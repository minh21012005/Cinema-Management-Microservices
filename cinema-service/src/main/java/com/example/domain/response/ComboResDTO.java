package com.example.domain.response;

import lombok.Data;

import java.util.List;

@Data
public class ComboResDTO {
    private Long id;
    private String code;
    private String name;
    private Double price;
    private String description;
    private boolean available;
    private String imageKey;
    private List<ComboFoodRes> foods;

    @Data
    public static class ComboFoodRes {
        private Long foodId;
        private String foodName;
        private int quantity;
    }
}

