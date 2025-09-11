package com.example.validator.impl;

import com.example.repository.RoleRepository;
import com.example.validator.UniqueRoleName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueRoleNameValidator implements ConstraintValidator<UniqueRoleName, String> {

    private final RoleRepository roleRepository;

    public UniqueRoleNameValidator(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // để @NotBlank xử lý
        }
        return !roleRepository.existsByName(value.trim());
    }
}
