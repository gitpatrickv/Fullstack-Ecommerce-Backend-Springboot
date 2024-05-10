package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.entity.Cart;
import com.practice.fullstackbackendspringboot.model.CartModel;
import com.practice.fullstackbackendspringboot.model.request.CartRequest;
import com.practice.fullstackbackendspringboot.service.CartService;
import com.practice.fullstackbackendspringboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @PutMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public CartModel addProductToCart(@RequestBody @Valid CartRequest cartRequest, @RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        return cartService.addProductToCart(cartRequest,user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CartModel> getAllProductsInCart(@RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        return cartService.getAllProductsInCart(user);
    }

}
