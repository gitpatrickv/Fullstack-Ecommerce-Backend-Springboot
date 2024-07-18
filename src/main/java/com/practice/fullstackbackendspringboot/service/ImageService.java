package com.practice.fullstackbackendspringboot.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    void uploadProductPhoto(String productId, MultipartFile[] files);
    void uploadUserPhoto(String email, MultipartFile file);
    void uploadStorePhoto(String email, String storeId, MultipartFile file);
    public byte[] getPhoto(String filename) throws IOException;

}
