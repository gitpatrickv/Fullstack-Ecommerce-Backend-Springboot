package com.practice.fullstackbackendspringboot.validation.impl;

import com.practice.fullstackbackendspringboot.repository.StoreRepository;
import com.practice.fullstackbackendspringboot.validation.UniqueStoreValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueStoreValidator implements ConstraintValidator<UniqueStoreValid, String> {

    private final StoreRepository storeRepository;

    @Override
    public void initialize(UniqueStoreValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String storeName, ConstraintValidatorContext constraintValidatorContext) {
        return storeRepository.findByStoreNameIgnoreCase(storeName).isEmpty();
    }
}
