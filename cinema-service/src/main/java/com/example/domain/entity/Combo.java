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

    @Column(nullable = false, unique = true, length = 20)
    private String code; // Mã định danh combo (VD: CB001)

    @Column(nullable = false)
    private String name; // Tên combo

    @Column(nullable = false)
    private Double price; // Giá combo

    private String description; // Mô tả combo

    @Column(nullable = false)
    private String imageKey;

    @Column(nullable = false)
    private boolean available = true; // Còn bán hay không

    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComboFood> comboFoods;
}
