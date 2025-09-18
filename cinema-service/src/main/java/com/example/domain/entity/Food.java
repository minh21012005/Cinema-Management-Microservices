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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Tên món ăn

    @Column(nullable = false)
    private Double price; // Giá món ăn

    private String description; // Mô tả món ăn

    @Column(nullable = false)
    private boolean available = true; // Trạng thái: còn bán hay hết hàng
}
