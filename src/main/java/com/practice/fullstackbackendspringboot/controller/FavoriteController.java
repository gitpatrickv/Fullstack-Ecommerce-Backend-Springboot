package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.model.FavoritesModel;
import com.practice.fullstackbackendspringboot.service.FavoritesService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final UserService userService;
    private final FavoritesService favoritesService;

    @PutMapping("/add/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void addToFavorites(@PathVariable (value="productId") String productId){
        String user = userService.getAuthenticatedUser();
        favoritesService.addToFavorites(user,productId);
    }

    @PutMapping("/cart/add")
    @ResponseStatus(HttpStatus.OK)
    public void addToFavoritesByFilter(){
        String user = userService.getAuthenticatedUser();
        favoritesService.addToFavoritesByFilter(user);
    }
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<AllProductModel> getAllFavorites() {
        String user = userService.getAuthenticatedUser();
        return favoritesService.getAllFavorites(user);
    }

    @GetMapping("/get/status/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public FavoritesModel getFavoriteStatus(@PathVariable (value="productId") String productId){
        String user = userService.getAuthenticatedUser();
        return favoritesService.getFavoriteStatus(user,productId);
    }
}
