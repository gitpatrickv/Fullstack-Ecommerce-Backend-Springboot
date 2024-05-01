package com.practice.fullstackbackendspringboot.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductImageService {

    void uploadPhoto(String id, MultipartFile file);
    public byte[] getPhoto(String filename) throws IOException;

}
