package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    void uploadPhoto(String id, MultipartFile file);
}
