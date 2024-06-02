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
    public void addToFavorites(@RequestHeader("Authorization") String email, @PathVariable (value="productId") String productId){
        String user = userService.getUserFromToken(email);
        favoritesService.addToFavorites(user,productId);
    }
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<AllProductModel> getAllFavorites(@RequestHeader("Authorization") String email) {
        String user = userService.getUserFromToken(email);
        return favoritesService.getAllFavorites(user);
    }

    @GetMapping("/get/status/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public FavoritesModel getFavoriteStatus(@RequestHeader("Authorization") String email, @PathVariable (value="productId") String productId){
        String user = userService.getUserFromToken(email);
        return favoritesService.getFavoriteStatus(user,productId);
    }
}
