package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.request.ChangePasswordRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdateUserRequest;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.AccountService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UpdateUserRequest updateAccountInfo(String email, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND));

        user.setName(updateUserRequest.getName() != null ? updateUserRequest.getName() : user.getName());
        user.setAddress(updateUserRequest.getAddress() != null ? updateUserRequest.getAddress() : user.getAddress());
        user.setContactNumber(updateUserRequest.getContactNumber() != null ? updateUserRequest.getContactNumber() : user.getContactNumber());
        userRepository.save(user);

        return UpdateUserRequest.builder()
                .name(user.getName())
                .address(user.getAddress())
                .contactNumber(user.getContactNumber())
                .build();
    }

    @Override
    public void changePassword(String email, ChangePasswordRequest request, Principal user) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {

            User newPassword = (User) ((UsernamePasswordAuthenticationToken) user).getPrincipal();

            if (!passwordEncoder.matches(request.getOldPassword(), newPassword.getPassword())) {
                throw new BadCredentialsException(StringUtil.WRONG_PASSWORD);
            }
            if (!request.getNewPassword().matches(request.getConfirmPassword())) {
                throw new BadCredentialsException(StringUtil.PASSWORD_NOT_MATCH);
            }
            newPassword.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(newPassword);
        } else {
            throw new UsernameNotFoundException(StringUtil.USER_NOT_FOUND);
        }
    }
}
