package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "foods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food extends BaseEntity<Long> {

    @Column(nullable = false, unique = true, length = 20)
    private String code; // Mã định danh món ăn (VD: FD001)

    @Column(nullable = false)
    private String name; // Tên món ăn

    @Column(nullable = false)
    private Double price; // Giá món ăn

    private String description; // Mô tả món ăn

    @Column(nullable = false)
    private boolean available = true; // Trạng thái: còn bán hay hết hàng

    @Column(nullable = false)
    private String imageKey;

    // Liên kết với loại món ăn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_type_id", nullable = false)
    private FoodType type;
}
