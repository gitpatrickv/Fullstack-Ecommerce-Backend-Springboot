package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.service.ProductImageService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;
    private final UserService userService;

    @PostMapping("/product/image/upload")
    public void uploadProductPhoto(@RequestParam(value = "id") String id, @RequestParam(value = "file") MultipartFile file) {
        productImageService.uploadProductPhoto(id, file);
    }
    @PostMapping("/user/image/upload")
    public void uploadUserPhoto(@RequestHeader("Authorization") String email,  @RequestParam(value = "file") MultipartFile file) {
        String username = userService.getUserFromToken(email);
        productImageService.uploadUserPhoto(username,file);
    }

    @GetMapping(path = "/product/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getProductPhoto(@PathVariable("filename") String filename) throws IOException {
        return productImageService.getPhoto(filename);
    }

    @GetMapping(path = "/user/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getUserPhoto(@PathVariable("filename") String filename) throws IOException {
        return productImageService.getPhoto(filename);
    }

}
