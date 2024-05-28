package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.request.UpdateUserRequest;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.AccountService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final UserService userService;
    @Override
    public UpdateUserRequest updateAccountInfo(String email, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByEmail(email).get();

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
}
