package com.practice.fullstackbackendspringboot.validation;

import com.practice.fullstackbackendspringboot.validation.impl.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordValid {

    String message() default "{password.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
