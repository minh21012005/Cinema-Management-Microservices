package com.example.validator;

import com.example.validator.impl.UniqueRoleNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueRoleNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueRoleName {
    String message() default "Role name already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
