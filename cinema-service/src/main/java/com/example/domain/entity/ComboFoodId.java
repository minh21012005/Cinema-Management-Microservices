package com.example.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboFoodId implements java.io.Serializable {
    private Long comboId;
    private Long foodId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComboFoodId that)) return false;
        return comboId.equals(that.comboId) && foodId.equals(that.foodId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(comboId, foodId);
    }
}

