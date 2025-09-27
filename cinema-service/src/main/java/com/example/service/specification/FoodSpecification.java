package com.example.service.specification;

import com.example.domain.entity.Food;
import org.springframework.data.jpa.domain.Specification;

public class FoodSpecification {
    public static Specification<Food> findFoodWithFilters(String name, Long typeId) {
        Specification<Food> spec = (root, query, cb) -> cb.conjunction(); // mặc định luôn true

        if (name != null && !name.isEmpty()) {
            spec = spec.and(
                    (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (typeId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("type").get("id"), typeId));
        }

        return spec;
    }
}
