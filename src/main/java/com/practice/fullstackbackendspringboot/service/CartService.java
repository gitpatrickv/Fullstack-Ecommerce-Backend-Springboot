package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.entity.Cart;
import com.practice.fullstackbackendspringboot.model.CartModel;
import com.practice.fullstackbackendspringboot.model.CartTotalModel;
import com.practice.fullstackbackendspringboot.model.request.CartRequest;
import com.practice.fullstackbackendspringboot.model.request.QuantityRequest;

import java.util.List;

public interface CartService {

    CartModel addProductToCart(CartRequest cartRequest, String email);
    List<CartModel> getAllProductsInCart(String email);
    CartTotalModel getCartTotal(String email, boolean filter);
    CartTotalModel filterCartProducts(String cartId, String email);
    CartTotalModel filterAllCartProducts(String email);
    void increaseQuantity(QuantityRequest quantityRequest, String email);
    void decreaseQuantity(QuantityRequest quantityRequest, String email);
    void delete(String cartId, String email);

}
