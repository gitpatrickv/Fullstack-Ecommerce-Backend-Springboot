package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.entity.ProductImage;
import com.practice.fullstackbackendspringboot.repository.ProductImageRepository;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.service.ProductImageService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;

    private final ProductRepository productRepository;

    @Override
    public void uploadPhoto(String id, MultipartFile file) {
        log.info("Saving picture for product ID: {}", id);

        Optional<Product> product = productRepository.findById(id);

        ProductImage productImage = new ProductImage();
        productImage.setProduct(product.get());
        productImage.setPhotoUrl(processImage(id, file));
        productImageRepository.save(productImage);
        log.info(productImage.getPhotoUrl());
    }

    @Override
    public byte[] getPhoto(String filename) throws IOException {
        return Files.readAllBytes(Paths.get(StringUtil.PHOTO_DIRECTORY + filename));
    }

    private String processImage(String id, MultipartFile image) {
        String filename = id+image.getOriginalFilename();
        try {
            Path fileStorageLocation = Paths.get(StringUtil.PHOTO_DIRECTORY).toAbsolutePath().normalize();

            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename));

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/product/image/" + filename).toUriString();
        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    }

}
