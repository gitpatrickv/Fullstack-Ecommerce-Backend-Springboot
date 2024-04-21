package com.practice.fullstackbackendspringboot.validation.impl;

import com.practice.fullstackbackendspringboot.model.UserModel;
import com.practice.fullstackbackendspringboot.validation.ConfirmPasswordValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPasswordValid, Object> {
    @Override
    public void initialize(ConfirmPasswordValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        UserModel model = (UserModel) object;

        return model.getPassword().equals(model.getConfirmPassword());
    }
}