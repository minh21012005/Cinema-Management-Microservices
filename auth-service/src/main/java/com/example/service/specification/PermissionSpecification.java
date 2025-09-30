package com.example.service.specification;

import com.example.domain.entity.Permission;
import org.springframework.data.jpa.domain.Specification;

public class PermissionSpecification {
    public static Specification<Permission> findPermissionsWithFilters(String module) {
        Specification<Permission> spec = (root, query, cb) -> cb.conjunction();

        if (module != null && !module.isEmpty()) {
            spec = spec.and(
                    (root, query, cb) -> cb.like(cb.lower(root.get("module")), "%" + module.toLowerCase() + "%")
            );
        }

        return spec;
    }
}
