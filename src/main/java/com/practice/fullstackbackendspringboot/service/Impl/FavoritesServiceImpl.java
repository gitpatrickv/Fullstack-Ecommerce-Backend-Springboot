package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.*;
import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.repository.*;
import com.practice.fullstackbackendspringboot.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoritesServiceImpl implements FavoritesService {

    private final UserRepository userRepository;
    private final FavoritesRepository favoritesRepository;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public void addToFavorites(String email, String productId) {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Product> product = productRepository.findById(productId);
        Optional<Image> image = imageRepository.findByProduct_ProductId(productId);
        Optional<Inventory> inventory = inventoryRepository.findByProduct_ProductId(productId);
        Optional<Favorites> favorite = favoritesRepository.findByProductIdAndUserEmail(productId, user.get().getEmail());

        if(favorite.isPresent() && favorite.get().isFavorites()){
            favoritesRepository.delete(favorite.get());
        }else{
            Favorites favorites = new Favorites();
            favorites.setPhotoUrl(image.get().getPhotoUrl());
            favorites.setProductId(product.get().getProductId());
            favorites.setPrice(inventory.get().getPrice());
            favorites.setProductName(product.get().getProductName());
            favorites.setFavorites(true);
            favorites.setUser(user.get());
            favoritesRepository.save(favorites);
        }
    }

    @Override
    public List<AllProductModel> getAllFavorites(String email) {
        List<Favorites> favorites = favoritesRepository.findAllByUserEmail(email);
        List<AllProductModel> model = new ArrayList<>();

        for(Favorites favorite : favorites){
            AllProductModel favoritesModel = new AllProductModel();
            favoritesModel.setProductName(favorite.getProductName());
            favoritesModel.setPrice(favorite.getPrice());
            favoritesModel.setPhotoUrl(favorite.getPhotoUrl());
            favoritesModel.setFavorites(favorite.isFavorites());
            favoritesModel.setProductId(favorite.getProductId());

            model.add(favoritesModel);
        }

        return model;
    }
}
