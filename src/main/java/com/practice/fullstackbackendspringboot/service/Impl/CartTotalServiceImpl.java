package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.CartTotal;
import com.practice.fullstackbackendspringboot.model.CartTotalModel;
import com.practice.fullstackbackendspringboot.repository.CartTotalRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.CartTotalService;
import com.practice.fullstackbackendspringboot.service.UserService;
import com.practice.fullstackbackendspringboot.utils.mapper.CartTotalMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class CartTotalServiceImpl implements CartTotalService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final CartTotalRepository cartTotalRepository;
    private final CartTotalMapper cartTotalMapper;
    @Override
    public CartTotalModel getCartTotal(String email) {
        userRepository.findByEmail(email);
        Optional<CartTotal> total = cartTotalRepository.findByUserEmail(email);

        CartTotal cartTotal = total.get();

        return cartTotalMapper.mapEntityToModel(cartTotal);
    }
}
