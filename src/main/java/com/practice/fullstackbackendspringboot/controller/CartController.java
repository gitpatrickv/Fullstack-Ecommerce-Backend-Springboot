package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.CartModel;
import com.practice.fullstackbackendspringboot.model.CartTotalModel;
import com.practice.fullstackbackendspringboot.model.request.CartRequest;
import com.practice.fullstackbackendspringboot.model.request.CartVariationRequest;
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
    public CartModel addProductToCart(@RequestBody @Valid CartRequest cartRequest){
        String user = userService.getAuthenticatedUser();
        return cartService.addProductToCart(cartRequest,user);
    }

    @PutMapping("/add/variations")
    @ResponseStatus(HttpStatus.CREATED)
    public CartModel addProductWithVariationToCart(@RequestBody @Valid CartVariationRequest cartRequest){
        String user = userService.getAuthenticatedUser();
        return cartService.addProductWithVariationToCart(cartRequest,user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CartModel> getAllProductsInCart(){
        String user = userService.getAuthenticatedUser();
        return cartService.getAllProductsInCart(user);
    }
    @GetMapping("/total")
    @ResponseStatus(HttpStatus.OK)
    public CartTotalModel getCartTotal(){
        String user = userService.getAuthenticatedUser();
        return cartService.getCartTotal(user,true);
    }

    @PutMapping("/filter/{cartId}")
    @ResponseStatus(HttpStatus.OK)
    public void filterCartProducts(@PathVariable("cartId") String cartId){
        cartService.filterCartProducts(cartId);
    }
    @PutMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public void filterAllCartProducts() {
        String user = userService.getAuthenticatedUser();
        cartService.filterAllCartProducts(user);
    }
    @PutMapping("/filter/store/{storeId}")
    public void filterCartByStoreName(@PathVariable("storeId") String storeId){
        String user = userService.getAuthenticatedUser();
        cartService.filterCartByStoreName(storeId,user);
    }
    @GetMapping("/checkout")
    public List<CartModel> checkout(){
        String user = userService.getAuthenticatedUser();
        return cartService.checkout(user);
    }

    @PutMapping("/increment")
    @ResponseStatus(HttpStatus.OK)
    public void increaseQuantity(@RequestBody QuantityRequest quantityRequest) {
        String user = userService.getAuthenticatedUser();
        cartService.increaseQuantity(quantityRequest,user);
    }

    @PutMapping("/decrement")
    @ResponseStatus(HttpStatus.OK)
    public void decreaseQuantity(@RequestBody QuantityRequest quantityRequest) {
        String user = userService.getAuthenticatedUser();
        cartService.decreaseQuantity(quantityRequest,user);
    }

    @DeleteMapping("/delete/{cartId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String cartId) {
        cartService.delete(cartId);
    }
    @DeleteMapping("/delete")
    public void deleteAllCarts() {
        String user = userService.getAuthenticatedUser();
        cartService.deleteAllCarts(user);
    }

}
