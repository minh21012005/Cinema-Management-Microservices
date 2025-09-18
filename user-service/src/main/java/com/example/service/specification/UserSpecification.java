package com.example.service.specification;

import com.example.domain.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> findUsersWithFilters(String email, Long roleId) {
        Specification<User> spec = (root, query, cb) -> cb.conjunction(); // mặc định luôn true

        if (email != null && !email.isEmpty()) {
            spec = spec.and(
                    (root, query, cb) -> cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }

        if (roleId != null) {
            spec = spec.and(
                    (root, query, cb) -> cb.equal(root.get("roleId"), roleId));
        }

        return spec;
    }
}
