package com.example.service.specification;

import com.example.domain.entity.Role;
import org.springframework.data.jpa.domain.Specification;

public class RoleSpecification {
    public static Specification<Role> findRoleWithFilters(String code) {
        Specification<Role> spec = (root, query, cb) -> cb.conjunction();

        if (code != null && !code.isEmpty()) {
            spec = spec.and(
                    (root, query, cb) -> cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%")
            );
        }

        return spec;
    }
}
