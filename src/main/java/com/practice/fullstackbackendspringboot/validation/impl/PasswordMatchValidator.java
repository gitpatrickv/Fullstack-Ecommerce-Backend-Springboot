package com.practice.fullstackbackendspringboot.validation.impl;

import com.practice.fullstackbackendspringboot.model.request.ChangePasswordRequest;
import com.practice.fullstackbackendspringboot.validation.PasswordMatchValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatchValid, Object> {

    @Override
    public void initialize(PasswordMatchValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        ChangePasswordRequest request = (ChangePasswordRequest) object;

        if(request.getNewPassword().length() < 8 || request.getNewPassword().length() > 20 || request.getNewPassword().isBlank()) {
            return false;
        }
        if(request.getOldPassword().equals(request.getNewPassword())){
            return false;
        }
        return request.getNewPassword().equals(request.getConfirmPassword());


    }
}