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
public class ComboFood {

    @EmbeddedId
    private ComboFoodId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("comboId") // map comboId từ embedded id
    @JoinColumn(name = "combo_id", nullable = false)
    private Combo combo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("foodId") // map foodId từ embedded id
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(nullable = false)
    private int quantity; // số lượng món ăn trong combo
}


