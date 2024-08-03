package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.StoreModel;
import com.practice.fullstackbackendspringboot.model.request.CreateStoreRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdateShopInfoRequest;
import com.practice.fullstackbackendspringboot.model.response.PaginateStoreResponse;
import com.practice.fullstackbackendspringboot.model.response.StoreCount;
import com.practice.fullstackbackendspringboot.service.StoreService;
import com.practice.fullstackbackendspringboot.service.UserService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {

    private final UserService userService;
    private final StoreService storeService;

    @PostMapping("/store/create")
    public ResponseEntity<?> createStore(@RequestBody @Valid CreateStoreRequest request){
        try {
            String user = userService.getAuthenticatedUser();
            storeService.createStore(request,user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(StringUtil.StoreNameExists, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/store")
    public StoreModel getStoreInfo(){
            String user = userService.getAuthenticatedUser();
            return storeService.getStoreInfo(user);
    }

    @PutMapping("/store/update/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateShopInfo(@PathVariable String storeId, @RequestBody @Valid UpdateShopInfoRequest request){
        storeService.updateShopInfo(storeId,request);
    }

    @GetMapping("/store/list")
    @ResponseStatus(HttpStatus.OK)
    public PaginateStoreResponse getAllStores(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
                                              @RequestParam(defaultValue = "true", required = false) String sortBy){
        return storeService.getAllStores(pageNo,pageSize,sortBy);
    }
    @GetMapping("/store/count")
    public StoreCount getStoreCount() {
        return storeService.getStoreCount();
    }
    @PutMapping("/store/suspend/{storeId}")
    public void suspendStoreAndProductListing(@PathVariable String storeId) {
        String user = userService.getAuthenticatedUser();
        storeService.suspendStoreAndProductListing(storeId,user);
    }
}
