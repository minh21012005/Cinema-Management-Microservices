package com.example.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class ComboReqDTO {
    private String name;
    private Double price;
    private String description;
    private boolean available;
    private String imageKey;
    private List<ComboFoodItem> foods;

    @Data
    public static class ComboFoodItem {
        private Long foodId;
        private int quantity;
    }
}

