package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.AllProductModel;

import java.util.List;

public interface FavoritesService {

    void addToFavorites(String email, String productId);
    List<AllProductModel> getAllFavorites(String email);
}
