package com.practice.fullstackbackendspringboot.validation;

import com.practice.fullstackbackendspringboot.validation.impl.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmailValid {

    String message() default "{email.exist}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
