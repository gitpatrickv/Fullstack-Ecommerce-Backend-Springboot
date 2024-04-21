package com.practice.fullstackbackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.practice.fullstackbackendspringboot.entity.constants.Role;
import com.practice.fullstackbackendspringboot.validation.ConfirmPasswordValid;
import com.practice.fullstackbackendspringboot.validation.UniqueEmailValid;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConfirmPasswordValid
@JsonInclude(NON_DEFAULT)
public class UserModel{

    @Valid

    @UniqueEmailValid
    @NotBlank(message = "{email.required}")
    private String email;
    @NotBlank(message = "{name.required}")
    private String name;
    @NotBlank(message = "{address.required}")
    private String address;
    @NotBlank(message = "{phone.number.required}")
    private String contactNumber;
    @NotBlank(message = "{password.required}")
    private String password;
    @NotBlank(message = "{confirm.password.required}")
    private String confirmPassword;
    @Enumerated(EnumType.STRING)
    private Role role;
}
