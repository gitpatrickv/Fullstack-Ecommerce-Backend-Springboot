package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.model.FavoritesModel;

import java.util.List;

public interface FavoritesService {

    void addToFavorites(String email, String productId);
    void addToFavoritesByFilter(String email);
    List<AllProductModel> getAllFavorites(String email);
    FavoritesModel getFavoriteStatus(String email, String productId);
}
