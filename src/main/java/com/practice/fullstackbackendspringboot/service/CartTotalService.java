package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.CartTotalModel;

public interface CartTotalService {

    CartTotalModel getCartTotal(String email);
}
