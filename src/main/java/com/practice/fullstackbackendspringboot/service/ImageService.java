package com.practice.fullstackbackendspringboot.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    void uploadProductPhoto(String productId, MultipartFile[] files);
    void uploadUserPhoto(String email, MultipartFile file);
    public byte[] getPhoto(String filename) throws IOException;

}
