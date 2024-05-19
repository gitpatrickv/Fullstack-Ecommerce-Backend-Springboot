package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.*;
import com.practice.fullstackbackendspringboot.model.CartModel;
import com.practice.fullstackbackendspringboot.model.CartTotalModel;
import com.practice.fullstackbackendspringboot.model.request.CartRequest;
import com.practice.fullstackbackendspringboot.model.request.QuantityRequest;
import com.practice.fullstackbackendspringboot.repository.*;
import com.practice.fullstackbackendspringboot.service.CartService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.CartMapper;
import com.practice.fullstackbackendspringboot.utils.mapper.CartTotalMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductImageRepository productImageRepository;
    private final CartTotalRepository cartTotalRepository;
    private final CartMapper cartMapper;
    private final CartTotalMapper cartTotalMapper;

    @Override
    public CartModel addProductToCart(CartRequest cartRequest, String email) {

        Optional<User> user = userRepository.findByEmail(email);
        Optional<Product> product = productRepository.findById(cartRequest.getProductId());
        Optional<ProductImage> productImage = productImageRepository.findByProduct_ProductId(cartRequest.getProductId());
        Optional<Inventory> inventory = inventoryRepository.findByProduct_ProductId(cartRequest.getProductId());
        Optional<Cart> existingCart = cartRepository.findByProduct_ProductIdAndUserEmail(cartRequest.getProductId(),email);
        Cart cart;
        if(existingCart.isPresent()){
            cart = existingCart.get();

            if(cart.getQuantity() < inventory.get().getQuantity() &&
                    cartRequest.getQuantity() < inventory.get().getQuantity()){

                cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
                cart.setTotalAmount(cart.getQuantity() * inventory.get().getPrice());
                cartRepository.save(cart);
            }else{
                log.info(StringUtil.OUT_OF_STOCK);
            }
        }else{
            cart = new Cart();

            if(cartRequest.getQuantity() > inventory.get().getQuantity()){
                log.info(StringUtil.OUT_OF_STOCK);
            }else{
                cart.setProduct(product.get());
                cart.setQuantity(cartRequest.getQuantity());
                cart.setPrice(inventory.get().getPrice());
//                cart.setShopName(product.get().getShopName());
                cart.setProductName(product.get().getProductName());
                cart.setPhotoUrl(productImage.get().getPhotoUrl());
                cart.setTotalAmount(inventory.get().getPrice() * cartRequest.getQuantity());
                cart.setUser(user.get());
                cartRepository.save(cart);
            }
        }

        return cartMapper.mapCartEntityToCartModel(cart);
    }

    @Override
    public List<CartModel> getAllProductsInCart(String email) {
        return cartRepository.findAll()
                .stream()
                .filter(filter -> filter.getUser().getEmail().equals(email))
                .map(cartMapper::mapCartEntityToCartModel)
                .toList();
    }

    @Override
    public void filterCartProducts(String cartId, String email) {
        userRepository.findByEmail(email);
        Optional<Cart> existingCart = cartRepository.findByCartIdAndUserEmail(cartId,email);

        if(existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setFilter(!cart.isFilter());
            cartRepository.save(cart);
        }
    }

    @Override
    public void filterAllCartProducts(String email) {
        userRepository.findByEmail(email);
        List<Cart> existingCart = cartRepository.findAllByUserEmail(email);

        boolean allFilteredCarts = existingCart.stream().allMatch(Cart::isFilter);
        boolean toggleFilter = !allFilteredCarts;

        for(Cart cart : existingCart){
            cart.setFilter(toggleFilter);
            cartRepository.save(cart);
        }
    }

    @Override
    public CartTotalModel getCartTotal(String email, boolean filter) {
        User user = userRepository.findByEmail(email).get();
        List<Cart> carts = cartRepository.findAllByFilterAndUserEmail(true,email);
        Optional<CartTotal> existingCartTotal = cartTotalRepository.findByUserEmail(email);
        Double total = 0.0;

        for(Cart cart : carts){
            Double cartTotalAmount = cart.getTotalAmount();
            total += cartTotalAmount;
        }

        if(existingCartTotal.isPresent()){
        CartTotal cartTotal = existingCartTotal.get();
        cartTotal.setCartTotal(total);
        cartTotal.setUser(user);
        cartTotalRepository.save(cartTotal);
        return cartTotalMapper.mapEntityToModel(cartTotal);
        }

        CartTotal cartTotal = new CartTotal();
        cartTotal.setCartTotal(total);
        cartTotal.setUser(user);
        cartTotalRepository.save(cartTotal);
        return cartTotalMapper.mapEntityToModel(cartTotal);

    }

    @Override
    public void increaseQuantity(QuantityRequest quantityRequest, String email) {
        userRepository.findByEmail(email);
        Optional<Cart> existingCart = cartRepository.findByCartIdAndUserEmail(quantityRequest.getCartId(), email);
        Optional<Inventory> inventory = inventoryRepository.findByProduct_ProductId(quantityRequest.getProductId());

        if (existingCart.isPresent()){
            Cart cart = existingCart.get();
            if(cart.getQuantity() < inventory.get().getQuantity()) {
                cart.setQuantity(existingCart.get().getQuantity() + 1);
                cart.setTotalAmount(existingCart.get().getQuantity() * inventory.get().getPrice());
                cartRepository.save(cart);
            }else{
                log.info(StringUtil.OUT_OF_STOCK);
            }
        }
    }

    @Override
    public void decreaseQuantity(QuantityRequest quantityRequest, String email) {
        userRepository.findByEmail(email);
        Optional<Cart> existingCart = cartRepository.findByCartIdAndUserEmail(quantityRequest.getCartId(), email);
        Optional<Inventory> inventory = inventoryRepository.findByProduct_ProductId(quantityRequest.getProductId());

        if(existingCart.isPresent()){
            Cart cart = existingCart.get();
            if(cart.getQuantity() > 1) {
                cart.setQuantity(existingCart.get().getQuantity() - 1);
                cart.setTotalAmount(existingCart.get().getQuantity() * inventory.get().getPrice());
                cartRepository.save(cart);
            }else{
                cartRepository.deleteByCartIdAndUserEmail(quantityRequest.getCartId(), email);
            }
        }
    }

    @Override
    public void delete(String cartId, String email) {
        userRepository.findByEmail(email);
        cartRepository.deleteById(cartId);
    }


}

