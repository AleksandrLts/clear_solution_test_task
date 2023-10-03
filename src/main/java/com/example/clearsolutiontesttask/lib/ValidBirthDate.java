package com.example.clearsolutiontesttask.lib;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBirthDate {
    String message() default "Your age should be more than 18 years old";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
