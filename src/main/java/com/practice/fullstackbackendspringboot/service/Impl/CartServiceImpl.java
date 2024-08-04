package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.*;
import com.practice.fullstackbackendspringboot.model.CartModel;
import com.practice.fullstackbackendspringboot.model.CartTotalModel;
import com.practice.fullstackbackendspringboot.model.request.CartRequest;
import com.practice.fullstackbackendspringboot.model.request.CartVariationRequest;
import com.practice.fullstackbackendspringboot.model.request.QuantityRequest;
import com.practice.fullstackbackendspringboot.repository.*;
import com.practice.fullstackbackendspringboot.service.CartService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.CartMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final ImageRepository imageRepository;
    private final StoreRepository storeRepository;
    private final CartMapper cartMapper;

    @Override
    public CartModel addProductToCart(CartRequest cartRequest, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Product product = productRepository.findById(cartRequest.getProductId()).orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND));
        Inventory inventory = inventoryRepository.findByProduct_ProductId(cartRequest.getProductId()).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<Cart> existingCart = cartRepository.findByProduct_ProductIdAndUserEmail(cartRequest.getProductId(),email);
        Cart cart;
        if(existingCart.isPresent()){
            cart = existingCart.get();

            if(cart.getQuantity() < inventory.getQuantity() &&
                    cartRequest.getQuantity() < inventory.getQuantity()){

                cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
                cart.setTotalAmount(cart.getQuantity() * inventory.getPrice());
                cartRepository.save(cart);
            }else{
                throw new IllegalArgumentException(StringUtil.OUT_OF_STOCK);
            }
        }else{
            cart = new Cart();

            if(cartRequest.getQuantity() > inventory.getQuantity()){
                throw new IllegalArgumentException(StringUtil.OUT_OF_STOCK);
            }else{
                cart.setProduct(product);
                cart.setQuantity(cartRequest.getQuantity());
                cart.setStoreId(product.getStore().getStoreId());
                cart.setTotalAmount(inventory.getPrice() * cartRequest.getQuantity());
                cart.setUser(user);
                cart.setInventory(inventory);
                cartRepository.save(cart);
            }
        }

        return cartMapper.mapCartEntityToCartModel(cart);
    }

    @Override
    public CartModel addProductWithVariationToCart(CartVariationRequest cartRequest, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Product product = productRepository.findById(cartRequest.getProductId()).orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND));
        Optional<Inventory> inventory = inventoryRepository.findByColorsAndSizesAndProduct_ProductId(cartRequest.getColors(),cartRequest.getSizes(), cartRequest.getProductId());
        Optional<Cart> existingCart = cartRepository.findByColorsAndSizesAndProduct_ProductIdAndUserEmail(cartRequest.getColors(), cartRequest.getSizes(),cartRequest.getProductId(),email);
        Cart cart;
        if(existingCart.isPresent()){
            cart = existingCart.get();

            if(cart.getQuantity() < inventory.get().getQuantity() &&
                    cartRequest.getQuantity() < inventory.get().getQuantity()){

                cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
                cart.setTotalAmount(cart.getQuantity() * inventory.get().getPrice());
                cartRepository.save(cart);
            }else{
                throw new IllegalArgumentException(StringUtil.OUT_OF_STOCK);
            }
        }else{
            cart = new Cart();

            if(cartRequest.getQuantity() > inventory.get().getQuantity()){
                throw new IllegalArgumentException(StringUtil.OUT_OF_STOCK);
            }else{
                cart.setProduct(product);
                cart.setQuantity(cartRequest.getQuantity());
                cart.setStoreId(product.getStore().getStoreId());
                cart.setTotalAmount(inventory.get().getPrice() * cartRequest.getQuantity());
                cart.setColors(cartRequest.getColors());
                cart.setSizes(cartRequest.getSizes());
                cart.setUser(user);
                cart.setInventory(inventory.get());
                cartRepository.save(cart);
            }
        }

        return cartMapper.mapCartEntityToCartModel(cart);
    }

    @Override
    public List<CartModel> getAllProductsInCart(String email) {
        return cartRepository.findAllByUserEmailOrderByCreatedDateDesc(email)
                .stream()
                .map(cart -> {
                    Optional<Product> optionalProduct = productRepository.findByProductIdAndListedTrueAndSuspendedFalseAndDeletedFalse(cart.getProduct().getProductId());
                    if(optionalProduct.isPresent()) {
                        Product product = optionalProduct.get();

                        CartModel cartModel = cartMapper.mapCartEntityToCartModel(cart);
                        inventoryRepository.findById(cart.getInventory().getInventoryId())
                                .ifPresent(inventory -> {
                                    cartModel.setStockRemaining(inventory.getQuantity());
                                    cartModel.setPrice(inventory.getPrice());
                                });
                        cartModel.setProductName(product.getProductName());
                        cartModel.setStoreName(product.getStore().getStoreName());
                        cartModel.setPhotoUrl(product.getImage().get(0).getPhotoUrl());
                        return cartModel;

                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void filterCartProducts(String cartId) {

        Optional<Cart> existingCart = cartRepository.findById(cartId);

        if(existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setFilter(!cart.isFilter());
            cartRepository.save(cart);
        }
    }

    @Override
    public void filterAllCartProducts(String email) {

        List<Cart> existingCart = cartRepository.findAllByUserEmail(email);

        boolean allFilteredCarts = existingCart.stream().allMatch(Cart::isFilter);
        boolean toggleFilter = !allFilteredCarts;

        for(Cart cart : existingCart){
            cart.setFilter(toggleFilter);
            cartRepository.save(cart);
        }
    }

    @Override
    public void filterCartByStoreName(String storeId, String email) {

        List<Cart> existingCart = cartRepository.findAllByStoreIdAndUserEmail(storeId, email);

        boolean allFilteredCarts = existingCart.stream().allMatch(Cart::isFilter);
        boolean toggleFilter = !allFilteredCarts;

        for(Cart cart : existingCart){
            cart.setFilter(toggleFilter);
            cartRepository.save(cart);
        }
    }

    @Override
    public List<CartModel> checkout(String email) {

        return cartRepository.findAllByFilterTrueAndUserEmailOrderByCreatedDateDesc(email)
                .stream()
                .map(cart -> {
                    Optional<Product> optionalProduct = productRepository.findByProductIdAndListedTrueAndSuspendedFalseAndDeletedFalse(cart.getProduct().getProductId());
                    if(optionalProduct.isPresent()) {
                        Product product = optionalProduct.get();

                        CartModel cartModel = cartMapper.mapCartEntityToCartModel(cart);
                        inventoryRepository.findById(cart.getInventory().getInventoryId())
                                .ifPresent(inventory -> {
                                    cartModel.setPrice(inventory.getPrice());
                                });
                        cartModel.setProductName(product.getProductName());
                        cartModel.setStoreName(product.getStore().getStoreName());
                        cartModel.setPhotoUrl(product.getImage().get(0).getPhotoUrl());

                        return cartModel;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public CartTotalModel getCartTotal(String email, boolean filter) {
        List<Cart> carts = cartRepository.findAllByFilterAndUserEmail(true,email);
        List<Cart> cartCount = cartRepository.findAllByUserEmail(email);

        Double total = 0.0;
        long count = 0;
        long filteredItem = 0;
        long numberOfProductFiltered = 0;
        Double totalShippingFee = 0.0;

        for(Cart cart : carts){
            Optional<Product> optionalProduct = productRepository.findByProductIdAndListedTrueAndSuspendedFalseAndDeletedFalse(cart.getProduct().getProductId());
            if(optionalProduct.isPresent()) {
                Double cartTotalAmount = cart.getTotalAmount();
                total += cartTotalAmount;

                long filterNumber = cart.getQuantity();
                filteredItem += filterNumber;

                long productFiltered = 1;
                numberOfProductFiltered += productFiltered;
            }
        }

        for(Cart cart : cartCount){
            Optional<Product> optionalProduct = productRepository.findByProductIdAndListedTrueAndSuspendedFalseAndDeletedFalse(cart.getProduct().getProductId());
            if(optionalProduct.isPresent()) {
                long itemCount = 1L;
                count += itemCount;
            }
        }

        Map<String, List<Cart>> cartsByStore = carts.stream()
                .collect(Collectors.groupingBy(Cart::getStoreId));

        for (Map.Entry<String, List<Cart>> cartMap : cartsByStore.entrySet()) {
            String storeId = cartMap.getKey();

            Optional<Store> store = storeRepository.findById(storeId);
            Double shipFee = store.get().getShippingFee();
            totalShippingFee+=shipFee;
        }

        CartTotalModel cartTotalModel = new CartTotalModel();
        cartTotalModel.setCartTotal(total);
        cartTotalModel.setCartItems(count);
        cartTotalModel.setQty(filteredItem);
        cartTotalModel.setNumberOfProductFiltered(numberOfProductFiltered);
        cartTotalModel.setTotalShippingFee(totalShippingFee);
        cartTotalModel.setTotalPayment(total + totalShippingFee);
        return cartTotalModel;

    }

    @Override
    public void increaseQuantity(QuantityRequest quantityRequest, String email) {

        Optional<Cart> existingCart = cartRepository.findByCartIdAndUserEmail(quantityRequest.getCartId(), email);
        Optional<Inventory> inventory = inventoryRepository.findById(quantityRequest.getInventoryId());

        if (existingCart.isPresent()){
            Cart cart = existingCart.get();
            if(cart.getQuantity() < inventory.get().getQuantity()) {
                cart.setQuantity(existingCart.get().getQuantity() + 1);
                cart.setTotalAmount(existingCart.get().getQuantity() * inventory.get().getPrice());
                cartRepository.save(cart);
            }
        }
    }

    @Override
    public void decreaseQuantity(QuantityRequest quantityRequest, String email) {

        Optional<Cart> existingCart = cartRepository.findByCartIdAndUserEmail(quantityRequest.getCartId(), email);
        Optional<Inventory> inventory = inventoryRepository.findById(quantityRequest.getInventoryId());

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
    public void delete(String cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public void deleteAllCarts(String email) {
        cartRepository.deleteAllByFilterTrueAndUserEmail(email);
    }
}

