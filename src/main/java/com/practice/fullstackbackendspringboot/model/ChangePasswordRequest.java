package com.practice.fullstackbackendspringboot.model;

import com.practice.fullstackbackendspringboot.validation.PasswordMatchValid;
import com.practice.fullstackbackendspringboot.validation.PasswordValid;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatchValid
public class ChangePasswordRequest {
    @Valid

    @PasswordValid
    private String oldPassword;

    private String newPassword;

    private String confirmPassword;
}
