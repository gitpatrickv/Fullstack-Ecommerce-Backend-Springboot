package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/product/image")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping("/upload")
    public void uploadPhoto(@RequestParam(value = "id") String id, @RequestParam(value = "file") MultipartFile file) {
        productImageService.uploadPhoto(id, file);
    }

    @GetMapping(path = "/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return productImageService.getPhoto(filename);
    }

}
