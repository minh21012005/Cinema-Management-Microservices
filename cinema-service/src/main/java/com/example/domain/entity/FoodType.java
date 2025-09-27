package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "food_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodType extends BaseEntity<Long> {

    // code duy nhất, ví dụ: POPCORN, DRINK, SNACK
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    // tên hiển thị cho người dùng, ví dụ: "Bắp rang bơ", "Nước ngọt"
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;
}
