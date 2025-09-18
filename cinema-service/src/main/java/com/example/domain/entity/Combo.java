package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "combos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Combo extends BaseEntity<Long>{

    @Column(nullable = false)
    private String name; // Tên combo

    @Column(nullable = false)
    private Double price; // Giá combo

    private String description; // Mô tả combo

    @Column(nullable = false)
    private boolean available = true; // Còn bán hay không

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "combo_food",
            joinColumns = @JoinColumn(name = "combo_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> foods; // Danh sách món ăn trong combo
}
