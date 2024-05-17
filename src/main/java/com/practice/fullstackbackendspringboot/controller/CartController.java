package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.CartModel;
import com.practice.fullstackbackendspringboot.model.CartTotalModel;
import com.practice.fullstackbackendspringboot.model.request.CartRequest;
import com.practice.fullstackbackendspringboot.model.request.QuantityRequest;
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
    @ResponseStatus(HttpStatus.CREATED)
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
    @GetMapping("/total")
    @ResponseStatus(HttpStatus.OK)
    public CartTotalModel getCartTotal(@RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        return cartService.getCartTotal(user,true);
    }

    @PutMapping("/filter/{cartId}")
    @ResponseStatus(HttpStatus.OK)
    public void filterCartProducts(@PathVariable("cartId") String cartId,@RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        cartService.filterCartProducts(cartId,user);
    }
    @PutMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public void filterAllCartProducts(@RequestHeader("Authorization") String email) {
        String user = userService.getUserFromToken(email);
        cartService.filterAllCartProducts(user);
    }

    @PutMapping("/increment")
    @ResponseStatus(HttpStatus.OK)
    public void increaseQuantity(@RequestBody QuantityRequest quantityRequest, @RequestHeader("Authorization") String email) {
        String user = userService.getUserFromToken(email);
        cartService.increaseQuantity(quantityRequest,user);
    }

    @PutMapping("/decrement")
    @ResponseStatus(HttpStatus.OK)
    public void decreaseQuantity(@RequestBody QuantityRequest quantityRequest, @RequestHeader("Authorization") String email) {
        String user = userService.getUserFromToken(email);
        cartService.decreaseQuantity(quantityRequest,user);
    }

    @DeleteMapping("/{cartId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String cartId, @RequestHeader("Authorization") String email) {
        String user = userService.getUserFromToken(email);
        cartService.delete(cartId,email);
    }

}
