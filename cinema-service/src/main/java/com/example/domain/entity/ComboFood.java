package com.example.domain.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "combo_food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComboFood extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combo_id", nullable = false)
    private Combo combo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(nullable = false)
    private int quantity; // số lượng món ăn trong combo
}

