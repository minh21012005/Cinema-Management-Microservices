package com.example.service.specification;

import com.example.domain.entity.Combo;
import org.springframework.data.jpa.domain.Specification;

public class ComboSpecification {
    public static Specification<Combo> findComboWithFilters(String name) {
        Specification<Combo> spec = (root, query, cb) -> cb.conjunction();

        if (name != null && !name.isEmpty()) {
            spec = spec.and(
                    (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%")
            );
        }

        return spec;
    }
}
