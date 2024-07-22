package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.StoreModel;
import com.practice.fullstackbackendspringboot.model.request.CreateStoreRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdateShopInfoRequest;
import com.practice.fullstackbackendspringboot.model.response.StoreCount;
import com.practice.fullstackbackendspringboot.service.StoreService;
import com.practice.fullstackbackendspringboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {

    private final UserService userService;
    private final StoreService storeService;

    @PostMapping("/store/create")
    public ResponseEntity<?> createStore(@RequestBody @Valid CreateStoreRequest request, @RequestHeader("Authorization") String email){
        try {
            String user = userService.getUserFromToken(email);
            storeService.createStore(request,user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/store")
    @ResponseStatus(HttpStatus.OK)
    public StoreModel getStoreInfo(@RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        return storeService.getStoreInfo(user);
    }
    @PutMapping("/store/update/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateShopInfo(@RequestHeader("Authorization") String email, @PathVariable String storeId, @RequestBody @Valid UpdateShopInfoRequest request){
        String user = userService.getUserFromToken(email);
        storeService.updateShopInfo(user,storeId,request);
    }

    @GetMapping("/store/list")
    @ResponseStatus(HttpStatus.OK)
    public List<StoreModel> getAllStores(@RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        return storeService.getAllStores(user);
    }
    @GetMapping("/store/count")
    public StoreCount getStoreCount(@RequestHeader("Authorization") String email) {
        String user = userService.getUserFromToken(email);
        return  storeService.getStoreCount(user);
    }
    @PutMapping("/store/suspend/{storeId}")
    public void suspendStoreAndProductListing(@PathVariable String storeId, @RequestHeader("Authorization") String email) {
        String user = userService.getUserFromToken(email);
        storeService.suspendStoreAndProductListing(storeId,user);
    }
}
