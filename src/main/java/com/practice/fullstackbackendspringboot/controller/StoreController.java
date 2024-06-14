package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.StoreModel;
import com.practice.fullstackbackendspringboot.service.StoreService;
import com.practice.fullstackbackendspringboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final UserService userService;
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreModel> createStore(@RequestBody @Valid StoreModel storeModel, @RequestHeader("Authorization") String email){
        try {
            String user = userService.getUserFromToken(email);
            StoreModel createdStore = storeService.createStore(storeModel,user);
            return new ResponseEntity<>(createdStore, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public StoreModel getStoreInfo(String email){
        String user = userService.getUserFromToken(email);
        return storeService.getStoreInfo(user);
    }
}
