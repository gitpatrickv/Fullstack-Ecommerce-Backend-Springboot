package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.service.ImageService;
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

    private final ImageService imageService;
    private final UserService userService;

    @PostMapping("/product/image/upload")
    public void uploadProductPhoto(@RequestParam(value = "id") String id, @RequestParam(value = "file") MultipartFile file) {
        imageService.uploadProductPhoto(id, file);
    }
    @PostMapping("/user/image/upload")
    public void uploadUserPhoto(@RequestHeader("Authorization") String email,  @RequestParam(value = "file") MultipartFile file) {
        String user = userService.getUserFromToken(email);
        imageService.uploadUserPhoto(user,file);
    }

    @GetMapping(path = "/product/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getProductPhoto(@PathVariable("filename") String filename) throws IOException {
        return imageService.getPhoto(filename);
    }

    @GetMapping(path = "/user/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getUserPhoto(@PathVariable("filename") String filename) throws IOException {
        return imageService.getPhoto(filename);
    }

}
