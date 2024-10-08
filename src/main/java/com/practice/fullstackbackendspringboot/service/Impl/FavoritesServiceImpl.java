package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Cart;
import com.practice.fullstackbackendspringboot.entity.Favorites;
import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.model.FavoritesModel;
import com.practice.fullstackbackendspringboot.repository.CartRepository;
import com.practice.fullstackbackendspringboot.repository.FavoritesRepository;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.FavoritesService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.FavoritesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoritesServiceImpl implements FavoritesService {

    private final UserRepository userRepository;
    private final FavoritesRepository favoritesRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final FavoritesMapper favoritesMapper;
    @Override
    public void addToFavorites(String email, String productId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<Product> optionalProduct = productRepository.findByProductIdAndListedTrueAndSuspendedFalseAndDeletedFalse(productId);

        Optional<Favorites> favorite = favoritesRepository.findByProduct_ProductIdAndUserEmail(productId, user.getEmail());
        Favorites favorites;
        if(favorite.isPresent() && favorite.get().isFavorites()){
            favorites = favorite.get();
            favorites.setFavorites(false);
            favoritesRepository.delete(favorites);
        }else {
            if(optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                favorites = new Favorites();
                favorites.setFavorites(true);
                favorites.setProduct(product);
                favorites.setUser(user);
                favoritesRepository.save(favorites);
            } else {
                throw new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND + productId);
            }
        }
    }

    @Override
    public void addToFavoritesByFilter(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        List<Cart> carts = cartRepository.findAllByFilterAndUserEmail(true,email);

        for(Cart cart : carts){
            Optional<Favorites> favorite = favoritesRepository.findByProduct_ProductIdAndUserEmail(cart.getProduct().getProductId(), user.getEmail());
            Optional<Product> optionalProduct = productRepository.findByProductIdAndListedTrueAndSuspendedFalseAndDeletedFalse(cart.getProduct().getProductId());

            if(favorite.isPresent() && favorite.get().isFavorites()){
                cartRepository.delete(cart);
            }else {
                if(optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    Favorites favorites = new Favorites();
                    favorites.setFavorites(true);
                    favorites.setProduct(product);
                    favorites.setUser(user);
                    favoritesRepository.save(favorites);
                    cartRepository.delete(cart);
                }
            }
        }

    }

    @Override
    public List<AllProductModel> getAllFavorites(String email) {
        List<Favorites> favorites = favoritesRepository.findAllByUserEmail(email);
        List<AllProductModel> model = new ArrayList<>();

        for(Favorites favorite : favorites){
            Optional<Product> optionalProduct = productRepository.findByProductIdAndListedTrueAndSuspendedFalseAndDeletedFalse(favorite.getProduct().getProductId());

            if(optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                AllProductModel favoritesModel = new AllProductModel();
                favoritesModel.setProductName(product.getProductName());
                favoritesModel.setPrice(product.getInventory().iterator().next().getPrice());
                favoritesModel.setPhotoUrl(product.getImage().get(0).getPhotoUrl());
                favoritesModel.setProductId(product.getProductId());
                favoritesModel.setProductSold(product.getProductSold());

                model.add(favoritesModel);
            }
        }
        return model;
    }

    @Override
    public FavoritesModel getFavoriteStatus(String email, String productId) {

        Optional<Favorites> favorites = favoritesRepository.findByProduct_ProductIdAndUserEmail(productId,email);

        return favorites.map(favoritesMapper::mapEntityToModel).orElse(null);
    }

}
